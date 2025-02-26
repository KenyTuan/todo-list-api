package com.test.todoapp.converter;

import com.test.todoapp.dtos.auth.AuthRes;
import com.test.todoapp.dtos.auth.RegisterReq;
import com.test.todoapp.model.entity.User;
import com.test.todoapp.model.enums.ObjStatus;
import com.test.todoapp.model.enums.Role;


public class AuthConverter {

    /**
     * Converts a RegisterReq to a User.
     *
     * @param req The RegisterReq to convert.
     * @return The converted User.
     */
    public static User convertToEntity(RegisterReq req) {
        final User user = User
                .builder()
                .name(req.getName())
                .email(req.getEmail())
                .role(Role.MEMBER)
                .build();

        user.setObjStatus(ObjStatus.ACTIVE);
        return user;
    }


    /**
     * Converts a Token to a AuthRes.
     *
     * @param token The Token to convert.
     * @param user The User to convert.
     * @return The converted AuthRes(token, UserRes).
     */
    public static AuthRes covertToDto(String token, User user) {
        return new AuthRes(
                token,
                UserConverter.convertToDto(user)
        );
    }
}
