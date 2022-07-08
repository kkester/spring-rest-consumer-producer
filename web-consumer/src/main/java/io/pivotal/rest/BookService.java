package io.pivotal.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final WebClient webClient;

    public List<Book> getBooks() {
        return webClient
                .get()
                .uri("/books")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Book.class)
                .collectList().block();
    }

    public void generateBook() {
        log.info("Generating New Book");
        Book newBook = webClient.get()
                .uri("/books/new")
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(this::handleResponse)
                .block();

        webClient.post()
                .uri("/books")
                .bodyValue(newBook)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(clientResponse -> log.info("Book created {}", clientResponse.getHeaders().get(HttpHeaders.LOCATION)))
                .subscribe();
    }

    private Mono<Book> handleResponse(ClientResponse clientResponse) {
        Mono<Book> resultMono;
        if (HttpStatus.OK.equals(clientResponse.statusCode())) {
            resultMono = clientResponse.bodyToMono(Book.class);
        } else {
            resultMono = Mono.error(new RuntimeException());
        }
        return resultMono;
    }
}
