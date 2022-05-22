package io.pivotal.rest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;
import java.util.Map;

@Component
public class ErrorHandler {
    public Mono<ServerResponse> handleError(ConstraintViolationException violationError) {
        Map<String,Object> errorResponse = Map.of("code", "invalid-request");
        return ServerResponse.status(HttpStatus.UNPROCESSABLE_ENTITY).bodyValue(errorResponse);
    }

    public Mono<ServerResponse> operationFailed(Throwable error) {
        Map<String,Object> errorResponse = Map.of("code", "operation-failed");
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(errorResponse);
    }
}
