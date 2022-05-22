package io.pivotal.rest;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BookProviderService {

    private final BookRepository bookRepository;

    public Flux<Book> getAllBooks() {
        return bookRepository.findAll()
                .map(this::convert);
    }

    private Book convert(BookEntity bookEntity) {
        return Book.builder()
                .author(bookEntity.getAuthor())
                .cost(bookEntity.getCost())
                .title(bookEntity.getTitle())
                .build();
    }

    public Mono<Book> getNewBook() {
        Faker faker = new Faker();
        return Mono.just(Book.builder()
                .title(faker.book().title())
                .author(faker.book().author())
                .cost(faker.number().randomDouble(2, 9, 1000))
                .build());
    }

    public Mono<BookEntity> save(Book book) {
        BookEntity bookEntity = BookEntity.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .cost(book.getCost())
                .build();
        return bookRepository.save(bookEntity);
    }
}
