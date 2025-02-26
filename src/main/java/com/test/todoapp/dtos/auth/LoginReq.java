package com.test.todoapp.dtos.auth;

import com.test.todoapp.constants.MessageException;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Builder
public class LoginReq {

    @Pattern(message = MessageException.EMAIL_INVALID,
            regexp = "^(?!.*@.*@)(?!.*\\.\\.)(?!.*@\\.)(?!.*\\.$)(?!^\\.)(?!.*\\s)[^@]+@[^@]+\\.[^@]+$")
    private String email;

    @Pattern(message = MessageException.PASS_INVALID,
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[\\W_]).{8,}$")
    private String password;
}
