package de.workshops.bookshelf.books;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAllBooks();
    }

    Book getBookByIsbn(String isbn) {
        return bookRepository.findAllBooks().stream()
            .filter(book -> book.getIsbn().equals(isbn))
            .findFirst()
            .orElseThrow(() -> new BookException("No book for this ISBN: " + isbn));
    }

    List<Book> getBooksByAuthor(String author) {
        return bookRepository.findAllBooks().stream()
            .filter(book -> book.getAuthor().contains(author))
            .toList();
    }

    List<Book> searchBooks(BookSearchRequest searchRequest) {
        return bookRepository.findAllBooks().stream()
            .filter(book -> book.getTitle().contains(searchRequest.title())
                || book.getAuthor().contains(searchRequest.author()))
            .toList();
    }

    public Book addBook(@Valid Book book) {
        return bookRepository.save(book);
    }
}