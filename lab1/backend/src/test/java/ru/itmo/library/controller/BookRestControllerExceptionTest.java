package ru.itmo.library.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookRestControllerExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getNonExistentBook_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/books/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateNonExistentBook_shouldReturnNotFound() throws Exception {
        String bookJson = "{\"title\":\"Title\",\"author\":\"Author\",\"publicationYear\":2023,\"isbn\":\"1234567890\"}";

        mockMvc.perform(put("/api/books/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createBookWithInvalidData_shouldReturnBadRequest() throws Exception {
        String invalidBookJson = "{\"title\":\"\",\"author\":\"\",\"publicationYear\":0,\"isbn\":\"\"}";

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBookJson))
                .andExpect(status().isBadRequest());
    }
}