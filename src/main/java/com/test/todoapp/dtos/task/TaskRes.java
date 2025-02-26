package com.test.todoapp.dtos.task;

import java.time.LocalDateTime;

public record TaskRes(
        String id,
        String title,
        String description,
        String userId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
