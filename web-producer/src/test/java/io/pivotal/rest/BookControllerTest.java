package io.pivotal.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BookProviderService bookProviderService;

    @Test
    void testGetAllBooks() throws Exception {
        Book book = Book.builder().title("The Title").author("Mr Author").cost(1.99).build();
        when(bookProviderService.getAllBooks()).thenReturn(List.of(book));

        MvcResult mvcResult = this.mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andReturn();

        List<Book> results = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(results).containsExactly(book);
    }

    @Test
    void testGetBookById() throws Exception {
        Book book = Book.builder().title("The Title").author("Mr Author").cost(1.99).build();
        UUID id = UUID.randomUUID();
        when(bookProviderService.getBookById(id)).thenReturn(book);

        MvcResult mvcResult = this.mockMvc.perform(get("/books/{0}", id))
                .andExpect(status().isOk())
                .andReturn();

        Book result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Book.class);
        assertThat(result).isEqualTo(book);
    }

    @Test
    void testCreateBook() throws Exception {
        Book book = Book.builder().title("The Title").author("Mr Author").cost(1.99).build();
        UUID id = UUID.randomUUID();
        BookEntity bookEntity = BookEntity.builder().id(id).build();
        when(bookProviderService.save(book)).thenReturn(bookEntity);

        this.mockMvc.perform(post("/books")
                        .content(objectMapper.writeValueAsBytes(book))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/books/"+id));
    }

    @Test
    void testCreateBookUsingInvalidRequest() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/books")
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        ErrorResponse errorResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);
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
    }
}