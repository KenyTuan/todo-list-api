package com.test.todoapp.exception;

import com.fasterxml.jackson.core.JsonParseException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalException {

    /**
     * Handles all exceptions that are not specifically handled.
     *
     * @param request - HttpServletRequest containing information about the request.
     * @param ex - Exception thrown.
     * @return ExceptionRes - Object containing error information.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionRes handleException(HttpServletRequest request, Exception ex) {
        log.error("Internal server error", ex);
        return buildResponse(ErrorCode.GENERIC_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, request, ex);
    }

    /**
     * Handles client-related errors (e.g. unsupported media type, JSON parse error, etc.).
     *
     * @param request - HttpServletRequest containing information about the request.
     * @param ex - Exception thrown.
     * @return ExceptionRes - Object containing error information.
     */
    @ExceptionHandler({
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            JsonParseException.class,
            MethodNotAllowedException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentNotValidException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionRes handleClientErrors(HttpServletRequest request, Exception ex) {
        if (ex instanceof HttpMediaTypeNotSupportedException) {
            log.warn("Unsupported media type", ex);
            return buildResponse(ErrorCode.HTTP_MEDIA_TYPE_NOT_SUPPORTED, HttpStatus.UNSUPPORTED_MEDIA_TYPE, request, ex);
        } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
            log.warn("Media type not acceptable", ex);
            return buildResponse(ErrorCode.HTTP_MEDIA_TYPE_NOT_ACCEPTABLE, HttpStatus.NOT_ACCEPTABLE, request, ex);
        } else if (ex instanceof HttpMessageNotReadableException) {
            log.warn("Message not readable", ex);
            return buildResponse(ErrorCode.HTTP_MESSAGE_NOT_READABLE, HttpStatus.BAD_REQUEST, request, ex);
        } else if (ex instanceof HttpMessageNotWritableException) {
            log.error("Message not writable", ex);
            return buildResponse(ErrorCode.HTTP_MESSAGE_NOT_WRITABLE, HttpStatus.INTERNAL_SERVER_ERROR, request, ex);
        } else if (ex instanceof JsonParseException) {
            log.warn("JSON parse error", ex);
            return buildResponse(ErrorCode.JSON_PARSE_ERROR, HttpStatus.BAD_REQUEST, request, ex);
        } else if (ex instanceof MissingServletRequestParameterException) {
            log.warn("Missing request parameter", ex);
            return buildResponse(ErrorCode.MISSING_PARAMETER, HttpStatus.BAD_REQUEST, request, ex);
        } else if (ex instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            log.warn("Method argument not valid", methodArgumentNotValidException);
            String message = methodArgumentNotValidException
                    .getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return new ExceptionRes(
                    ErrorCode.CONSTRAINT_VIOLATION.getErrCode(),
                    message,
                    HttpStatus.BAD_REQUEST.value(),
                    request.getRequestURL().toString(),
                    request.getMethod(),
                    Instant.now()
            );
        }
        return buildResponse(ErrorCode.GENERIC_ERROR, HttpStatus.BAD_REQUEST, request, ex);
    }


    /**
     * Handles errors related to resources not being found (e.g. NotFoundException).
     *
     * @param request - HttpServletRequest containing information about the request.
     * @param ex - Exception thrown.
     * @return ExceptionRes - Object containing error information.
     */
    @ExceptionHandler({
            InvalidRefreshTokenException.class,
            NotFoundException.class,
            NoHandlerFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionRes handleNotFoundErrors(HttpServletRequest request, Exception ex) {
        if (ex instanceof InvalidRefreshTokenException) {
            log.warn("Invalid refresh token", ex);
            return buildResponse(ErrorCode.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND, request, ex);
        } else if (ex instanceof NoHandlerFoundException) {
            log.warn("No handler found", ex);
            return buildResponse(ErrorCode.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND, request, ex);
        } else if (ex instanceof NotFoundException notFoundEx) {
            return new ExceptionRes(
                    notFoundEx.getErrCode(),
                    notFoundEx.getErrMsg(),
                    HttpStatus.NOT_FOUND.value(),
                    request.getRequestURL().toString(),
                    request.getMethod(),
                    Instant.now()
            );
        }
        return buildResponse(ErrorCode.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND, request, ex);
    }

    /**
     * Handles errors related to conflicts (e.g. GenericAlreadyException) and invalid requests (e.g. BadRequestException).
     *
     * @param request - HttpServletRequest containing information about the request.
     * @param ex - Exception thrown.
     * @return ExceptionRes - Object containing error information.
     */
    @ExceptionHandler({
            GenericAlreadyException.class,
            BadRequestException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionRes handleConflictAndBadRequestErrors(HttpServletRequest request, Exception ex) {
        if (ex instanceof GenericAlreadyException alreadyEx) {
            log.warn("Resource already exists", ex);
            return new ExceptionRes(
                    alreadyEx.getErrCode(),
                    alreadyEx.getErrMsg(),
                    HttpStatus.CONFLICT.value(),
                    request.getRequestURL().toString(),
                    request.getMethod(),
                    Instant.now()
            );
        } else if (ex instanceof BadRequestException badRequestEx) {
            return new ExceptionRes(
                    badRequestEx.getErrCode(),
                    badRequestEx.getErrMsg(),
                    HttpStatus.BAD_REQUEST.value(),
                    request.getRequestURL().toString(),
                    request.getMethod(),
                    Instant.now()
            );
        }
        return buildResponse(ErrorCode.GENERIC_ERROR, HttpStatus.BAD_REQUEST, request, ex);
    }

    /**
     * Handles errors related to authorization being denied (e.g. AccessDeniedException).
     *
     * @param request - HttpServletRequest containing information about the request.
     * @param ex - Exception thrown.
     * @return ExceptionRes - Object containing error information.
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionRes handleAuthorizationDeniedException(
            HttpServletRequest request,
            AccessDeniedException ex) {
        log.warn("Authorization denied", ex);
        return buildResponse(ErrorCode.ACCESS_DENIED, HttpStatus.FORBIDDEN, request, ex);
    }

    /**
     * Constructs an ExceptionRes object from the error information.
     *
     * @param errorCode - Error code.
     * @param status - Corresponding HttpStatus.
     * @param request - HttpServletRequest containing information about the request.
     * @param ex - The Exception thrown.
     * @return ExceptionRes - Object containing error information.
     */
    private ExceptionRes buildResponse(ErrorCode errorCode, HttpStatus status, HttpServletRequest request, Exception ex) {
        return new ExceptionRes(
                errorCode.getErrCode(),
                errorCode.getErrMessage() + ": " + ex.getMessage(),
                status.value(),
                request.getRequestURL().toString(),
                request.getMethod(),
                Instant.now()
        );
    }
}