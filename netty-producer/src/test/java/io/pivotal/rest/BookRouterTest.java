package io.pivotal.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BookRouter.class, BookHandler.class, ErrorHandler.class})
@WebFluxTest
class BookRouterTest {
    @Autowired
    ApplicationContext context;

    @MockBean
    BookProviderService bookProviderService;

    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void testGetAllBooks() {
        Book book = Book.builder().title("The Title").author("Mr Author").cost(1.99).build();
        when(bookProviderService.getAllBooks()).thenReturn(Flux.just(book));

        webTestClient.get()
                .uri("/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Book.class)
                .value(booksResponse -> assertThat(booksResponse).containsExactly(book));
    }

    @Test
    void testGetBookById() {
        Book book = Book.builder().title("The Title").author("Mr Author").cost(1.99).build();
        when(bookProviderService.getBookById(1L)).thenReturn(Mono.just(book));

        webTestClient.get()
                .uri("/books/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .isEqualTo(book);
    }

    @Test
    void testCreateBook() {
        Book book = Book.builder().title("The Title").author("Mr Author").cost(1.99).build();
        BookEntity bookEntity = BookEntity.builder().id(1L).build();
        when(bookProviderService.save(book)).thenReturn(Mono.just(bookEntity));

        webTestClient.post()
                .uri("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(book)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader()
                .location("/books/1");
    }

    @Test
    void testCreateBookUsingInvalidRequest() {
        Book book = Book.builder().build();
        webTestClient.post()
                .uri("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(book)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    assertThat(errorResponse.getCode()).isEqualTo("invalid-request");
                    List<ErrorResponse> errorResponses = List.of(
                            ErrorResponse.builder()
                                    .description("Title is required")
                                    .build(),
                            ErrorResponse.builder()
                                    .description("Cost is required")
                                    .build(),
                            ErrorResponse.builder()
                                    .description("Author is required")
                                    .build());
                    assertThat(errorResponse.getErrors()).containsExactlyInAnyOrderElementsOf(errorResponses);
                });
    }

}