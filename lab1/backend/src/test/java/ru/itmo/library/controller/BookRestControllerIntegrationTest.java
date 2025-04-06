package ru.itmo.library.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.itmo.library.model.Book;
import ru.itmo.library.repository.BookRepository;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        bookRepository.deleteAll();
    }

    @Test
    public void createBook_shouldReturnCreatedBook() throws Exception {
        String bookJson = "{\"title\":\"New Book\",\"author\":\"Author\",\"publicationYear\":2023,\"isbn\":\"1234567890\"}";

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("New Book")))
                .andExpect(jsonPath("$.author", is("Author")));
    }

    @Test
    public void getBookById_whenBookExists_shouldReturnBook() throws Exception {
        Book book = new Book(null, "Integration Test Book", null, null, null);
        book = bookRepository.save(book);

        mockMvc.perform(get("/api/books/" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Integration Test Book")));
    }

    @Test
    public void updateBook_shouldUpdateExistingBook() throws Exception {
        Book book = bookRepository.save(new Book(null, "Old Title", "Author", 2020, "ISBN"));

        String updatedBookJson = "{\"title\":\"Updated Title\",\"author\":\"New Author\",\"publicationYear\":2021,\"isbn\":\"NEWISBN\"}";

        mockMvc.perform(put("/api/books/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Title")))
                .andExpect(jsonPath("$.author", is("New Author")));
    }

    @Test
    public void deleteBook_shouldRemoveBook() throws Exception {
        Book book = bookRepository.save(new Book(null, "To Delete", "Author", 2020, "ISBN"));

        mockMvc.perform(delete("/api/books/" + book.getId()))
                .andExpect(status().isNoContent());

        assertFalse(bookRepository.existsById(book.getId()));
    }
}