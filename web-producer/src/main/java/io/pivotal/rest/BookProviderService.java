package io.pivotal.rest;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookProviderService {

    private final BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    private Book convert(BookEntity bookEntity) {
        return Book.builder()
                .author(bookEntity.getAuthor())
                .cost(bookEntity.getCost())
                .title(bookEntity.getTitle())
                .build();
    }

    public Book getNewBook() {
        Faker faker = new Faker();
        return Book.builder()
                .title(faker.book().title())
                .author(faker.book().author())
                .cost(faker.number().randomDouble(2, 9, 1000))
                .build();
    }

    public BookEntity save(Book book) {
        BookEntity bookEntity = BookEntity.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .cost(book.getCost())
                .build();
        return bookRepository.save(bookEntity);
    }

    public Book getBookById(UUID id) {
        return bookRepository.findById(id)
                .map(bookEntity -> Book.builder()
                        .title(bookEntity.getTitle())
                        .author(bookEntity.getAuthor())
                        .cost(bookEntity.getCost())
                        .build())
                .orElseThrow(() -> new ResourceNotFoundException("Book Not Found"));
    }
}
