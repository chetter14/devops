package ru.itmo.library.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.itmo.library.exception.BookNotFoundException;
import ru.itmo.library.model.Book;
import ru.itmo.library.repository.BookRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookRestControllerTest {

    @MockitoBean
    private BookRepository bookRepository;

    @InjectMocks
    private BookRestController bookController;


    @Test
    public void getAllBooks_shouldReturnAllBooks() {
        Book book1 = new Book(1L, "Book 1", "Author 1", 2020, "ISBN1");
        Book book2 = new Book(2L, "Book 2", "Author 2", 2021, "ISBN2");
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<Book> result = bookController.getBooks();

        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void getBookById_whenBookExists_shouldReturnBook() {
        Long bookId = 1L;
        Book book = new Book(bookId, "Test Book", "Test Author", 2023, "1234567890");
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Book response = bookController.getBookById(bookId);

        assertEquals(book, response);
    }

    @Test
    public void getBookById_whenBookNotExists_shouldReturnNotFound() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookController.getBookById(bookId));
    }
}
