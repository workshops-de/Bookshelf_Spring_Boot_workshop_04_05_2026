package de.workshops.bookshelf.books;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books")
@Validated
public class BookRestController {
    private final BookService bookService;

    @Value("${app.name:Bücherregal}")
    private String applicationName;

    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{isbn}")
    Book getBookByIsbn(@PathVariable(name = "isbn") @Size(min = 10, max = 14) String anIsbn) {
        return bookService.getBookByIsbn(anIsbn);
    }

    @GetMapping(params = "author")
    ResponseEntity<List<Book>> getBooksByAuthor(@RequestParam String author) {
        var result = bookService.getBooksByAuthor(author);

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/search")
    List<Book> searchBooks(@RequestBody @Valid BookSearchRequest searchRequest) {
        return bookService.searchBooks(searchRequest);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    Book addBook(@RequestBody @Valid Book book) {
        return bookService.addBook(book);
    }

    @ExceptionHandler(BookException.class)
    ResponseEntity<String> handleBookException(BookException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}