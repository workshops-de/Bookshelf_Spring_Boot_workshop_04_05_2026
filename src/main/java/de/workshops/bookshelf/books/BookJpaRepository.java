package de.workshops.bookshelf.books;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookJpaRepository extends JpaRepository<Book, Long> {

    Book findByIsbn(String isbn);

    List<Book> findByAuthorContaining(String author);

    List<Book> searchByTitleContainingOrAuthorContaining(String title, String author);

    @Query("SELECT b FROM Book b WHERE b.title LIKE %?1% OR b.author LIKE %?2%")
    List<Book> searchBooks(String title, String author);
}