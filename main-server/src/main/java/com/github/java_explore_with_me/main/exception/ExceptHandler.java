package com.github.java_explore_with_me.main.exception;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptHandler {
    private final String reason = "Неправильно составленный запрос";

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError conflictException(ConflictException conflictException) {
        log.warn(conflictException.getMessage(), conflictException);
        return new ApiError(HttpStatus.CONFLICT.toString(),
                conflictException.getMessage(),
                reason,
                LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.warn(exception.getMessage(), exception);
        return new ApiError(HttpStatus.BAD_REQUEST.toString(),
                exception.getMessage(),
                reason,
                LocalDateTime.now());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError notFoundException(NotFoundException notFoundException) {
        log.warn(notFoundException.getMessage(), notFoundException);
        return new ApiError(HttpStatus.NOT_FOUND.toString(),
                notFoundException.getMessage(),
                reason,
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ApiError badRequestError(BadRequestException exception) {
        log.warn(exception.getMessage(), exception);
        return new ApiError(HttpStatus.BAD_REQUEST.toString(),
                exception.getMessage(),
                reason,
                LocalDateTime.now());
    }
}
