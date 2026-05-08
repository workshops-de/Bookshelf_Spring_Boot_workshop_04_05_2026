package de.workshops.bookshelf.books;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(BookJdbcRepository.class)
class BookJdbcRepositoryTest {
    @Autowired
    BookJdbcRepository repository;

    @Test
    void testSaveBook() {
        Book book = new Book();
        book.setTitle("Test Title");
        book.setDescription("Test Description");
        book.setAuthor("Test Author");
        book.setIsbn("1234567890");

        Book savedBook = repository.save(book);
        assertNotNull(savedBook.getId());
    }
}