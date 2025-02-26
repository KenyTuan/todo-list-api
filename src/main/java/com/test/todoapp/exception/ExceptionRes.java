package com.test.todoapp.exception;

import java.time.Instant;

public record ExceptionRes(
        String code,
        String message,
        Integer status,
        String url,
        String reqMethod,
        Instant timestamp
) {
}
