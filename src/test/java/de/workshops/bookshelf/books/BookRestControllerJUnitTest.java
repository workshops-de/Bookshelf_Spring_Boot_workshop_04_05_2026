package de.workshops.bookshelf.books;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookRestControllerJUnitTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookRestController bookRestController;

    @Test
    void getAllBooks() {
        when(bookService.getAllBooks()).thenReturn(List.of(new Book(), new Book()));

        assertThat(bookRestController.getAllBooks()).hasSize(2);
    }
    @Test
    void getAllBooks_withException() {
        doThrow(new BookException("Something went wrong")).when(bookService).getAllBooks();

        assertThatThrownBy(() ->bookRestController.getAllBooks())
            .isInstanceOf(BookException.class)
            .hasMessage("Something went wrong");
    }

    @Test
    void getBookByIsbn() {
        var book = new Book();
        book.setIsbn("978-0201633610");

        when(bookService.getBookByIsbn(anyString())).thenReturn(book);

        assertThat(bookRestController.getBookByIsbn("0815"))
            .isNotNull()
            .hasFieldOrPropertyWithValue("isbn", "978-0201633610");
    }
}