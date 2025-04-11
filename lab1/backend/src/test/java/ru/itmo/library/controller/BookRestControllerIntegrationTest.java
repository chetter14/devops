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

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void Should_ReturnBook_WhenAddBook() throws Exception {
        String bookJson = "{\"title\":\"New Book\",\"author\":\"Author\",\"publicationYear\":2023,\"isbn\":\"1234567890\"}";

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("New Book")))
                .andExpect(jsonPath("$.author", is("Author")));
    }

    @Test
    public void Should_ReturnBook_WhenGetBookById() throws Exception {
        Book book = new Book(null, "Integration Test Book", "Author", 2010, "1212");
        book = bookRepository.save(book);

        mockMvc.perform(get("/api/books/" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Integration Test Book")));
    }

    @Test
    public void Should_UpdateExistingBook_WhenUpdateBook() throws Exception {
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
    public void Should_NotExistsBook_WhenDeleteBook() throws Exception {
        Book book = bookRepository.save(new Book(null, "To Delete", "Author", 2020, "ISBN"));

        mockMvc.perform(delete("/api/books/" + book.getId())).andExpect(status().isNoContent());

        assertFalse(bookRepository.existsById(book.getId()));
    }

    @Test
    public void Should_ReturnNotFound_WhenFindNonExistingBook() throws Exception {
        mockMvc.perform(get("/api/books/9999")).andExpect(status().isNotFound());
    }

    @Test
    public void Should_ReturnNotFound_WhenUpdateNonExistentBook() throws Exception {
        String bookJson = "{\"title\":\"Title\",\"author\":\"Author\",\"publicationYear\":2023,\"isbn\":\"1234567890\"}";

        mockMvc.perform(put("/api/books/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void Should_ReturnBadRequest_WhenCreateBookWithInvalidData() throws Exception {
        String invalidBookJson = "{\"title\":\"\",\"author\":\"\",\"publicationYear\":0,\"isbn\":\"\"}";

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBookJson))
                .andExpect(status().isBadRequest());
    }
}
