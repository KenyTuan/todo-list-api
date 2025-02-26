package com.test.todoapp.dtos.user;


import com.test.todoapp.dtos.task.TaskRes;

import java.time.LocalDateTime;
import java.util.Set;

public record UserRes(
        String id,
        String name,
        String email,
        Set<TaskRes>  tasks,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
