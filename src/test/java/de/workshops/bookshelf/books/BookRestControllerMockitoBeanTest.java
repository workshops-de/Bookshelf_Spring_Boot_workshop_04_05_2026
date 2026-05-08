package de.workshops.bookshelf.books;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest
class BookRestControllerMockitoBeanTest {

    @MockitoBean
    private BookService bookService;

    @Autowired
    private BookRestController bookRestController;

    @Captor
    private ArgumentCaptor<String> isbnCaptor;

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

        when(bookService.getBookByIsbn(isbnCaptor.capture())).thenReturn(book);

        assertThat(bookRestController.getBookByIsbn("1234567890"))
            .isNotNull()
            .hasFieldOrPropertyWithValue("isbn", "978-0201633610");

        var value = isbnCaptor.getValue();
        assertThat(value).isEqualTo("1234567890");

        verify(bookService).getBookByIsbn(anyString());
    }

}