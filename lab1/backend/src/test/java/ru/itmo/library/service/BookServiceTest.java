package ru.itmo.library.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itmo.library.exception.BookNotFoundException;
import ru.itmo.library.model.Book;
import ru.itmo.library.repository.BookRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void Should_ReturnSavedBook_WhenAddBook() {
        Book newBook = new Book(null, "New Book", "Author", 2023, "1234567890");
        Book savedBook = new Book(1L, "New Book", "Author", 2023, "1234567890");
        when(bookRepository.save(newBook)).thenReturn(savedBook);

        Book result = bookService.add(newBook);

        assertEquals(savedBook, result);
    }

    @Test
    void Should_ReturnBook_WhenBookExists() {
        Long bookId = 1L;
        Book expectedBook = new Book(bookId, "Existing Book", "Author", 2022, "0987654321");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(expectedBook));

        Book result = bookService.getBookById(bookId);

        assertEquals(expectedBook, result);
    }

    @Test
    void Should_ThrowException_WhenBookNotExists() {
        Long bookId = 99L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(bookId));
    }

    @Test
    void Should_ReturnAllBooks_WhenBooksExist() {
        Book book1 = new Book(1L, "Book 1", "Author 1", 2020, "1111111111");
        Book book2 = new Book(2L, "Book 2", "Author 2", 2021, "2222222222");

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        List<Book> result = bookService.getBooks();

        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(book1, book2)));
    }

    @Test
    void Should_UpdateBook_WhenBookExists() {
        Long bookId = 1L;
        Book existingBook = new Book(bookId, "Old Title", "Old Author", 2020, "OLDISBN");
        Book updatedData = new Book(bookId, "New Title", "New Author", 2023, "NEWISBN");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        Book result = bookService.update(updatedData);

        assertEquals(updatedData, result);
    }

    @Test
    void Should_ThrowException_WhenUpdatingNonExistingBook() {
        Long bookId = 99L;
        Book updatedData = new Book(bookId, "Title", "Author", 2023, "ISBN");

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.update(updatedData));
    }

    @Test
    void Should_ThrowException_WhenDeletingNonExistingBook() {
        Long bookId = 99L;

        when(bookRepository.existsById(bookId)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.delete(bookId));
    }
}
