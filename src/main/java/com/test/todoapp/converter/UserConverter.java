package com.test.todoapp.converter;

import com.test.todoapp.dtos.user.UserRes;
import com.test.todoapp.model.entity.User;

public class UserConverter {

    /**
     * Converts a User to a UserRes.
     *
     * @param user The User to convert.
     * @return The converted UserRes.
     */
    public static UserRes convertToDto(User user) {
        return new UserRes(
                user.getId(),
                user.getName(),
                user.getEmail(),
                TaskConverter
                        .convertToDtoList(user.getTasks()),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
