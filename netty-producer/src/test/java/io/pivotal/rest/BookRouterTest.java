package io.pivotal.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

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
                .value(booksResponse -> assertThat(booksResponse.get(0)).isEqualTo(book));
    }

}