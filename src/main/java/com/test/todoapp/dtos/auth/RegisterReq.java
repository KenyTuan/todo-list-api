package com.test.todoapp.dtos.auth;

import com.test.todoapp.constants.MessageException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder @Getter
public class RegisterReq {

    @NotBlank(message = MessageException.NAME_INVALID)
    private String name;

    @Pattern(message = MessageException.EMAIL_INVALID,
            regexp = "^(?!.*@.*@)(?!.*\\.\\.)(?!.*@\\.)(?!.*\\.$)(?!^\\.)(?!.*\\s)[^@]+@[^@]+\\.[^@]+$")
    private String email;

    @Pattern(message = MessageException.PASS_INVALID,
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[\\W_]).{8,}$")
    private String password;
}
