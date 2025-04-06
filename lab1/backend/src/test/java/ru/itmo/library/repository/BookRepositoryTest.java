package ru.itmo.library.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.itmo.library.model.Book;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void whenFindById_thenReturnBook() {
        Book book = new Book(null, "Test Book", "Test Author", 2023, "1234567890");
        bookRepository.save(book);

        Book found = bookRepository.findById(book.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    public void whenInvalidId_thenReturnNull() {
        Book fromDb = bookRepository.findById(-11L).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    public void whenDeleteById_thenShouldNotExist() {
        Book book = new Book(null, "To be deleted", null, null, null);
        bookRepository.save(book);

        bookRepository.deleteById(book.getId());

        assertThat(bookRepository.existsById(book.getId())).isFalse();
    }
}
