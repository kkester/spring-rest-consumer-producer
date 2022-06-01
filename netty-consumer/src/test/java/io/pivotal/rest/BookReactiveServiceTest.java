package io.pivotal.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
class BookReactiveServiceTest {

    static MockWebServer mockWebServer = new MockWebServer();

    @Autowired
    BookReactiveService bookService;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    StoreStocker storeStocker;

    @BeforeAll
    static void startMockServer() throws Exception {
        mockWebServer.start(8083);
    }

    @AfterAll
    static void stopMockServer() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void getBooks_shouldReturnAllBooks() throws Exception {
        Book book = Book.builder().title("XP").build();
        Book book1 = Book.builder().title("OODP").build();
        List<Book> expectedBooks = List.of(book);
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(objectMapper.writeValueAsString(expectedBooks)));

        System.out.println("Book equeals book1: " + book.equals(book1));
        Flux<Book> books = bookService.getBooks();
        StepVerifier.create(books)
                .assertNext(nextBook -> {
                    assertThat(nextBook).isEqualTo(book);
                })
                .expectComplete()
                .verify();
    }

}