package io.pivotal.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.net.URI;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class BookHandler {

    private final BookProviderService bookProviderService;
    private final ErrorHandler errorHandler;
    private final Validator validator;

    public Mono<ServerResponse> getNew(ServerRequest request) {
        return ServerResponse.ok().body(bookProviderService.getNewBook(), Book.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(Book.class)
                .flatMap(this::validate)
                .flatMap(bookProviderService::save)
                .flatMap(b -> ServerResponse.created(URI.create("/books/" + b.getId())).build())
                .onErrorResume(ConstraintViolationException.class, errorHandler::handleError)
                .onErrorResume(errorHandler::operationFailed);
    }

    private Mono<Book> validate(Book book) {
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        return violations.isEmpty() ? Mono.just(book) : Mono.error(new ConstraintViolationException(violations));
    }

    public Mono<ServerResponse> all(ServerRequest request) {
        return ServerResponse.ok().body(bookProviderService.getAllBooks(), Book.class);
    }
}
