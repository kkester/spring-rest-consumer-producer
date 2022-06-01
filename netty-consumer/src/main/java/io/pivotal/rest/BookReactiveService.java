package io.pivotal.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookReactiveService {

    private final WebClient webClient;

    public Flux<Book> getBooks() {
        return webClient
                .get()
                .uri("/books")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Book.class);
    }

    public void generateBook() {
        log.info("Generating New Book");
        webClient.get()
                .uri("/books/new")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Book.class)
                .doOnSuccess(book -> {
                    webClient.post()
                            .uri("/books")
                            .bodyValue(book)
                            .retrieve()
                            .toBodilessEntity()
                            .doOnSuccess(clientResponse -> log.info("Book created {}", clientResponse.getHeaders().get(HttpHeaders.LOCATION)))
                            .subscribe();
                })
                .subscribe();
    }
}
