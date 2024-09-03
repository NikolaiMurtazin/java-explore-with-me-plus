package ru.practicum.exeption;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        return new ApiError(
                HttpStatus.BAD_REQUEST.name(),
                "Incorrectly made request.",
                ex.getMessage() + extracted(ex),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler({WrongDateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleWrongDateException(final WrongDateException ex) {
        return new ApiError(
                HttpStatus.BAD_REQUEST.name(),
                "Incorrectly made request.",
                ex.getMessage() + extracted(ex),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(final ConflictException ex) {
        return new ApiError(
                HttpStatus.CONFLICT.name(),
                "Conflict occurred.",
                ex.getMessage() + extracted(ex),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException ex) {
        return new ApiError(
                HttpStatus.NOT_FOUND.name(),
                "The required object was not found.",
                ex.getMessage() + extracted(ex),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler({DataAccessException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleIllegalArgumentException(final Exception ex) {
        return new ApiError(
                HttpStatus.CONFLICT.name(),
                "Integrity constraint has been violated.",
                ex.getMessage() + extracted(ex),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler // вот с этим я хз. Так как в ApiError нет stackTrace
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable ex) {


        List<String> errors = Collections.singletonList(ex.getMessage());

        return new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                "An unexpected error occurred.",
                ex.getMessage() + extracted(ex),
                errors,
                LocalDateTime.now()
        );
    }

    private static String extracted(Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}
