package de.workshops.bookshelf.books;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookJdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    public BookJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    List<Book> findAllBooks() {
        var sql = "SELECT * FROM book";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Book.class));
    }

    public Book save(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        var sql = "INSERT INTO book (title, description, author, isbn) VALUES (?, ?, ?, ?)";
        int count = jdbcTemplate.update(con -> {
                var ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, book.getTitle());
                ps.setString(2, book.getDescription());
                ps.setString(3, book.getAuthor());
                ps.setString(4, book.getIsbn());
                return ps;}
            , keyHolder);

        if (count == 1) {
            book.setId((Long)keyHolder.getKeyList().getFirst().get("id"));
            return book;
        }
        throw new RuntimeException("Could not save book");
    }
}