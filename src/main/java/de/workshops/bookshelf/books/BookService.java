package de.workshops.bookshelf.books;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookJpaRepository bookRepository;

    public BookService(BookJpaRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    Book getBookByIsbn(String isbn) {
        var book = bookRepository.findByIsbn(isbn);
        if (book == null) {
            throw (new BookException("No book for this ISBN: " + isbn));
        }
        return book;
    }

    List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorContaining(author);
    }

    List<Book> searchBooks(BookSearchRequest searchRequest) {
        return bookRepository.searchBooks(searchRequest.title(), searchRequest.author());
    }

    public Book addBook(@Valid Book book) {
        return bookRepository.save(book);
    }
}