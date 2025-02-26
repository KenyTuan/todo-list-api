package com.test.todoapp.exception;

public class GenericAlreadyException extends CustomException{
    public GenericAlreadyException(String errorMessage) {
        super(ErrorCode.DUPLICATE_RESOURCE.getErrCode(), errorMessage);
    }
}
