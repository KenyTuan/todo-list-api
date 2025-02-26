package com.test.todoapp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    GENERIC_ERROR("PACKT-0001", "System error. Contact support."),
    CONSTRAINT_VIOLATION("PACKT-0002", "Validation failed."),
    ILLEGAL_ARGUMENT("PACKT-0003", "Invalid data provided."),
    RESOURCE_NOT_FOUND("PACKT-0004", "Resource not found."),
    DUPLICATE_RESOURCE("PACKT-0005", "Resource already exists."),
    ACCESS_DENIED("PACKT-0006", "Access denied."),
    UNAUTHORIZED("PACKT-0007", "Unauthorized access."),

    HTTP_METHOD_NOT_SUPPORTED("PACKT-0008", "HTTP method not supported."),
    HTTP_MEDIA_TYPE_NOT_SUPPORTED("PACKT-0009", "Unsupported media type. Use JSON or XML."),
    HTTP_MESSAGE_NOT_READABLE("PACKT-0010", "Invalid request payload. Ensure JSON/XML format."),
    HTTP_MESSAGE_NOT_WRITABLE("PACKT-0011", "Missing or invalid 'Accept' header."),
    HTTP_MEDIA_TYPE_NOT_ACCEPTABLE("PACKT-0012", "Unsupported 'Accept' header. Use JSON or XML."),
    JSON_PARSE_ERROR("PACKT-0013", "Make sure request payload should be a valid JSON object."),
    MISSING_PARAMETER("PACKT-0014", "Missing required parameter." ),

    TASK_NOT_FOUND("PACKT-0015", "Task not found!"),
    USER_NOT_FOUND("PACKT-0016", "User not found!"),
    EMAIL_ALREADY_EXISTS("PACKT-0017", "Email already exists!"),
    PASSWORD_INCORRECT("PACKT-0018", "Password incorrect!"),;

    private final String errCode;
    private final String errMessage;
}
