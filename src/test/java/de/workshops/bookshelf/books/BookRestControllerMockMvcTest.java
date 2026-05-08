package de.workshops.bookshelf.books;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NON_TEST)
//@WebMvcTest(controllers={BookRestController.class})
//@Import({BookService.class, BookJdbcRepository.class, TestConfig.class, JdbcTemplate.class})
@WithMockUser
class BookRestControllerMockMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllBooks() throws Exception {
        var mvcResult = mockMvc.perform(get("/books"))
            .andExpect(status().isOk())
            .andReturn();

        var payload = mvcResult.getResponse().getContentAsString();

        List<Book> books = objectMapper.readValue(payload, new TypeReference<>() {});

        assertThat(books).hasSize(3);
    }

    @Test
    void getBookByIsbn_isbnTooShort() throws Exception {
        mockMvc.perform(get("/books/9783"))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void getBookByIsbn_invalidIsbn() throws Exception {
        mockMvc.perform(get("/books/978-0201633611"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getBookByIsbn() throws Exception {
        var mvcResult = mockMvc.perform(get("/books/978-0201633610"))
            .andExpect(status().isOk())
            .andReturn();

        var payload = mvcResult.getResponse().getContentAsString();
        Book book = objectMapper.readValue(payload, Book.class);

        assertThat(book).isNotNull();
        assertThat(book.getIsbn()).isEqualTo("978-0201633610");
    }

    @Test
    void createBook_forbidden() throws Exception {
        mockMvc.perform(post("/books").with(csrf())
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBook() throws Exception {
        String isbn = "1111111111";
        String title = "My First Book";
        String author = "Birgit Kratz";
        String description = "A Must Read Book";

        var mvcResult = mockMvc.perform(post("/books").with(csrf())
                .content("""
                                {
                                    "isbn": "%s",
                                    "title": "%s",
                                    "author": "%s",
                                    "description": "%s"
                                }""".formatted(isbn, title, author, description))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        var payload = mvcResult.getResponse().getContentAsString();
        Book book = objectMapper.readValue(payload, Book.class);

        assertThat(book).isNotNull();
        assertThat(book.getIsbn()).isEqualTo(isbn);
        assertThat(book.getTitle()).isEqualTo(title);
        assertThat(book.getAuthor()).isEqualTo(author);
        assertThat(book.getDescription()).isEqualTo(description);
    }
}