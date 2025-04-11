package ru.itmo.library.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.itmo.library.model.Book;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void Should_ReturnBook_WhenSaveBook() {
        Book book = new Book(null, "Test Book", "Test Author", 2023, "1234567890");
        bookRepository.save(book);

        Book found = bookRepository.findById(book.getId()).orElse(null);

        assertEquals(book, found);
    }

    @Test
    public void Should_ReturnNull_WhenFindByInvalidId() {
        Book fromDb = bookRepository.findById(-11L).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    public void Should_NotExistsBook_WhenDeleteBook() {
        Book book = new Book(null, "To be deleted", "Author", 2010, "12121");
        bookRepository.save(book);

        bookRepository.deleteById(book.getId());

        assertThat(bookRepository.existsById(book.getId())).isFalse();
    }
}
