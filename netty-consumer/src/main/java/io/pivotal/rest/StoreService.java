package io.pivotal.rest;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final BookReactiveService bookService;
    private Store store;

    public Mono<Store> getStore() {
        if (store == null) {
            Faker faker = new Faker();
            store = Store.builder()
                    .name(faker.hipster().word())
                    .city(faker.address().city())
                    .state(faker.address().state())
                    .build();
        }
        return bookService.getBooks()
                .collect(Collectors.toList())
                .map(books -> store.toBuilder()
                        .books(books)
                        .build())
                .log();
    }
}
