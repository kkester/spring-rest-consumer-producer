package io.pivotal.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class BookServiceTest {

    static MockWebServer mockWebServer = new MockWebServer();

    @Autowired
    BookService bookService;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    StoreStocker storeStocker;

    @BeforeAll
    static void startMockServer() throws Exception {
        mockWebServer.start(8085);
    }

    @AfterAll
    static void stopMockServer() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void getBooks_shouldReturnAllBooks() throws Exception {
        List<Book> expectedBooks = List.of(Book.builder().title("XP").build());
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(objectMapper.writeValueAsString(expectedBooks)));

        List<Book> books = bookService.getBooks();

        assertThat(books).containsExactlyElementsOf(expectedBooks);
    }

}