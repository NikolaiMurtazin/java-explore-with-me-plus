package ru.practicum.exeption;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.info("400: Validation error: {}", e.getMessage());
        return new ApiError(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final ValidationException e) {
        log.info("400: Validation error: {}", e.getMessage());
        return new ApiError(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({WrongDateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final WrongDateException e) {
        log.info("400: Validation error: {}", e.getMessage());
        return new ApiError(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MissingServletRequestParameterException e) {
        log.info("400: Validation error: {}", e.getMessage());
        return new ApiError(HttpStatus.BAD_REQUEST, e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.info("404: {}", e.getMessage());
        return new ApiError(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        log.info("500 {}", e.getMessage(), e);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();

        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), stackTrace);
    }
}
