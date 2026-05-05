package de.workshops.bookshelf.books;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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
}