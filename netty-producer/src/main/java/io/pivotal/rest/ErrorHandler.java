package io.pivotal.rest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@Component
public class ErrorHandler {
    public Mono<ServerResponse> handleError(ConstraintViolationException violationError) {
        return ServerResponse.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .bodyValue(ErrorResponse.builder()
                        .code("invalid-request")
                        .errors(violationError.getConstraintViolations().stream()
                                .map(constraintViolation -> ErrorResponse.builder()
                                        .description(constraintViolation.getMessage())
                                        .build())
                                .collect(Collectors.toList()))
                        .build());
    }

    public Mono<ServerResponse> operationFailed(Throwable error) {
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue(ErrorResponse.builder()
                        .code("operation-failed")
                        .build());
    }

    public Mono<ServerResponse> handleNotFoundError(ResourceNotFoundException resourceNotFoundException) {
        return ServerResponse.status(HttpStatus.NOT_FOUND)
                .bodyValue(ErrorResponse.builder()
                        .code("not-found")
                        .description(resourceNotFoundException.getMessage())
                        .build());
    }

    public Mono<ServerResponse> handleNumberFormatError(NumberFormatException error) {
        return ServerResponse.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .bodyValue(ErrorResponse.builder()
                        .code("invalid-request")
                        .description(error.getMessage())
                        .build());
    }
}
