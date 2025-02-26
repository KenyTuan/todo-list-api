package com.test.todoapp.controller;

import com.test.todoapp.constants.APIEndPoint;
import com.test.todoapp.dtos.auth.AuthRes;
import com.test.todoapp.dtos.auth.LoginReq;
import com.test.todoapp.dtos.auth.RegisterReq;
import com.test.todoapp.dtos.user.UserRes;
import com.test.todoapp.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(APIEndPoint.PREFIX)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Handles user login requests.
     *
     * @param loginReq The login request containing user credentials (email and password).
     * @return An {@link AuthRes} object containing the JWT token and user details upon successful login.
     */
    @PostMapping(APIEndPoint.AUTH_V1 + "/login")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthRes loginUser(@Valid @RequestBody LoginReq loginReq) {
        return authService.loginUser(loginReq);
    }

    /**
     * Handles user registration requests.
     *
     * @param registerReq The registration request containing user details (e.g., name, email, password).
     * @return A {@link UserRes} object representing the newly registered user.
     */
    @PostMapping(APIEndPoint.AUTH_V1 + "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRes register(@Valid @RequestBody RegisterReq registerReq) {
        return authService.registerUser(registerReq);
    }
}
