package io.pivotal.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookProviderServiceMockitoTest {

    @InjectMocks
    BookProviderService bookProviderService;

    @Mock
    BookRepository bookRepository;

    @Captor
    ArgumentCaptor<BookEntity> bookEntityCaptor;

    @Test
    void save_successful_whenUsingValidBook() {
        // given
        Book book = Book.builder().build();

        // when
        bookProviderService.save(book);

        // then
        verify(bookRepository).save(bookEntityCaptor.capture());

        BookEntity actualBookEntity = bookEntityCaptor.getValue();
        assertThat(actualBookEntity).isEqualTo(BookEntity.builder().build());
    }

    @Test
    void save_fails_whenUnexpectedErrorOccurs() {
        // given
        Book book = Book.builder().build();
        doThrow(new RuntimeException()).when(bookRepository).save(any());
        //when(bookRepository.save(any())).thenThrow(new RuntimeException());

        // when
        bookProviderService.save(book);

        // then
        verify(bookRepository).save(bookEntityCaptor.capture());

        BookEntity actualBookEntity = bookEntityCaptor.getValue();
        assertThat(actualBookEntity).isEqualTo(BookEntity.builder().build());
    }

    @Test
    void getBookById_fails_whenBookEntityNotFound() {
        UUID id = UUID.randomUUID();
        when(bookRepository.findById(any())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
                bookProviderService.getBookById(id)
        );
        assertThat(resourceNotFoundException.getMessage()).isEqualTo("Book Not Found");
    }
}
