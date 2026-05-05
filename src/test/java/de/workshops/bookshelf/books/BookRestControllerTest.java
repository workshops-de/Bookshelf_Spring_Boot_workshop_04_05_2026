package de.workshops.bookshelf.books;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookRestControllerTest {

    @Autowired
    private BookRestController bookRestController;

    @Test
    void getAllBooks() {
        assertThat(bookRestController.getAllBooks()).hasSize(3);
    }

    @Test
    void getBookByIsbn() {
        assertThat(bookRestController.getBookByIsbn("978-0201633610")).isNotNull();
    }
}