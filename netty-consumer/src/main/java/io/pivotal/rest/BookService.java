package io.pivotal.rest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class BookService {

    public Flux<Book> getBooks() {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8083")
                .build();
        return webClient
                .get()
                .uri("/books")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Book.class);
    }
}
