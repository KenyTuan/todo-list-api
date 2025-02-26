package com.test.todoapp.service;

import com.test.todoapp.dtos.auth.AuthRes;
import com.test.todoapp.dtos.auth.LoginReq;
import com.test.todoapp.dtos.auth.RegisterReq;
import com.test.todoapp.dtos.user.UserRes;

public interface AuthService {

    UserRes registerUser(RegisterReq req);

    AuthRes loginUser(LoginReq req);
}
