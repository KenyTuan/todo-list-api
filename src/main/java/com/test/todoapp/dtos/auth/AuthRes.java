package com.test.todoapp.dtos.auth;

import com.test.todoapp.dtos.user.UserRes;

public record AuthRes(
        String token,
        UserRes user
) {
}
