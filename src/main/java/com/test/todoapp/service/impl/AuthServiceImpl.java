package com.test.todoapp.service.impl;

import com.test.todoapp.config.security.JwtUtil;
import com.test.todoapp.converter.AuthConverter;
import com.test.todoapp.converter.UserConverter;
import com.test.todoapp.dtos.auth.AuthRes;
import com.test.todoapp.dtos.auth.LoginReq;
import com.test.todoapp.dtos.auth.RegisterReq;
import com.test.todoapp.dtos.user.UserRes;
import com.test.todoapp.exception.BadRequestException;
import com.test.todoapp.exception.ErrorCode;
import com.test.todoapp.exception.NotFoundException;
import com.test.todoapp.model.entity.User;
import com.test.todoapp.repository.UserRepository;
import com.test.todoapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the {@link AuthService} interface.
 * This class provides methods for user authentication and registration.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authManager;

    /**
     * Registers a new user in the system.
     *
     * @param req The registration request containing user details.
     * @return A {@link UserRes} object representing the registered user.
     * @throws BadRequestException If the email is already registered.
     */
    @Override
    @Transactional
    public UserRes registerUser(RegisterReq req) {
        if (userRepository.existsByEmail(req.getEmail())){
            throw new BadRequestException(
                    ErrorCode.EMAIL_ALREADY_EXISTS.getErrCode(),
                    ErrorCode.EMAIL_ALREADY_EXISTS.getErrMessage());
        }

        final User user = AuthConverter.convertToEntity(req);

        user.setPassword(passwordEncoder.encode(req.getPassword()));

        return UserConverter.convertToDto(userRepository.save(user));
    }

    /**
     * Authenticates a user and generates a JWT token upon successful login.
     *
     * @param req The login request containing user credentials.
     * @return An {@link AuthRes} object containing the JWT token and user details.
     * @throws BadRequestException If the password is incorrect.
     * @throws NotFoundException If the user is not found.
     */
    @Override
    @Transactional
    public AuthRes loginUser(LoginReq req) {
        final User user = findUserByEmail(req.getEmail());

        if(!passwordEncoder.matches(req.getPassword(),
                user.getPassword())){
            throw new BadRequestException(
                    ErrorCode.PASSWORD_INCORRECT.getErrCode(),
                    ErrorCode.PASSWORD_INCORRECT.getErrMessage());
        }

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(),
                        req.getPassword()));

        return AuthConverter
                .covertToDto(jwtUtil.generateToken(user), user);
    }

    /**
     * Helper method to find a user by email.
     *
     * @param email The email of the user to find.
     * @return The {@link User} entity if found.
     * @throws NotFoundException If the user is not found.
     */
    private User findUserByEmail(String email) {
        return userRepository.findActiveByEmail(email)
                .orElseThrow(
                        () -> new NotFoundException(
                                ErrorCode.USER_NOT_FOUND.getErrMessage())
                );
    }
}
