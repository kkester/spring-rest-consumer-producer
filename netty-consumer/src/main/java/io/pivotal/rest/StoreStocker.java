package io.pivotal.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreStocker {

    private final BookService bookService;

    @Scheduled(fixedDelay = 5000)
    void stockStore() {
        bookService.generateBook();
    }
}
