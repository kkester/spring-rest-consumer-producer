package io.pivotal.rest;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    private static final String INVALID_ENUM_FORMAT_MESSAGE = "Invalid value: %s for the field %s. Valid values are %s";
    private static final String INVALID_FORMAT_MESSAGE = "Invalid value: %s for the field %s.";

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorResponse handleError(Exception error) {
        log.info("handling unexpected exception", error);
        return ErrorResponse.builder()
                .code("operation-failed")
                .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse handleNotFound(ResourceNotFoundException resourceNotFoundException) {
        log.debug("handling not found exception", resourceNotFoundException);
        return ErrorResponse.builder()
                .code("not-found")
                .description(resourceNotFoundException.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    ErrorResponse handleError(MethodArgumentNotValidException error) {
        log.debug("handling validation exception", error);
        return ErrorResponse.builder()
                .code("invalid-request")
                .errors(error.getFieldErrors().stream()
                        .map(constraintViolation -> ErrorResponse.builder()
                                .description(constraintViolation.getDefaultMessage())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    ErrorResponse handleError(HttpRequestMethodNotSupportedException error) {
        log.debug("handling unsupported exception", error);
        return ErrorResponse.builder()
                .code("invalid-method")
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleError(HttpMessageNotReadableException error) {

        String message;
        if (error.getCause() instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) error.getCause();
            String fieldName = invalidFormatException.getPath().stream().findFirst().map(JsonMappingException.Reference::getFieldName).orElse("NA");
            Object[] validValues = invalidFormatException.getTargetType().getEnumConstants();
            if (validValues == null) {
                message = String.format(INVALID_FORMAT_MESSAGE, invalidFormatException.getValue(), fieldName);
            } else {
                message = String.format(INVALID_ENUM_FORMAT_MESSAGE, invalidFormatException.getValue(), fieldName, Arrays.toString(validValues));
            }
        } else {
            message = error.getMessage();
        }

        return ErrorResponse.builder()
                .code("invalid-request")
                .description(message)
                .build();
    }
}
