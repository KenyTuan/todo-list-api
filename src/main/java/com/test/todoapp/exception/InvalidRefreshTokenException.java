package com.test.todoapp.exception;

public class InvalidRefreshTokenException extends CustomException{

    public InvalidRefreshTokenException(String message) {
        super(ErrorCode.UNAUTHORIZED.getErrCode(), message);
    }

}
