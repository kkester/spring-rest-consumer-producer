package io.pivotal.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {
    private final BookProviderService bookProviderService;

    @GetMapping
    List<Book> getAllBooks() {
        return bookProviderService.getAllBooks();
    }

    @PostMapping
    ResponseEntity<Void> createBook(@RequestBody @Valid Book book) {
        BookEntity bookEntity = bookProviderService.save(book);
        return ResponseEntity.created(URI.create("/books/"+bookEntity.getId())).build();
    }

    @GetMapping("/new")
    Book getNewBook() {
        return bookProviderService.getNewBook();
    }

    @GetMapping("/{bookId}")
    Book getBookById(@PathVariable UUID bookId) {
        return bookProviderService.getBookById(bookId);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleError(MethodArgumentTypeMismatchException error) {
        log.debug("handling invalid argument exception", error);
        return ErrorResponse.builder()
                .code("invalid-request")
                .description(error.getMessage())
                .build();
    }
}
