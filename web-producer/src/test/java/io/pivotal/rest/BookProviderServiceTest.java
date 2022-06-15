package io.pivotal.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class BookProviderServiceTest {

    BookProviderService bookProviderService;

    BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        bookProviderService = new BookProviderService(bookRepository);
    }

    @Test
    void save_successful_whenUsingValidBook() {
        // given
        Book book = Book.builder().build();

        // when
        bookProviderService.save(book);

        // then
        ArgumentCaptor<BookEntity> bookEntityCaptor = ArgumentCaptor.forClass(BookEntity.class);
        verify(bookRepository).save(bookEntityCaptor.capture());

        BookEntity actualBookEntity = bookEntityCaptor.getValue();
        assertThat(actualBookEntity).isEqualTo(BookEntity.builder().build());
    }
}