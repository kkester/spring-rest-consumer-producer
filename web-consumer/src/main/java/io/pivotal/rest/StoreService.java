package io.pivotal.rest;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final BookService bookService;
    private Store store;

    public Store getStore() {
        if (store == null) {
            Faker faker = new Faker();
            store = Store.builder()
                    .name(faker.hipster().word())
                    .city(faker.address().city())
                    .state(faker.address().state())
                    .build();
        }
        return store.toBuilder()
                .books(bookService.getBooks())
                .build();
    }
}
