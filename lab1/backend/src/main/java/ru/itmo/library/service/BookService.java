package ru.itmo.library.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.library.exception.BookNotFoundException;
import ru.itmo.library.model.Book;
import ru.itmo.library.repository.BookRepository;

import io.prometheus.client.Counter;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book add(Book book) {
        book.setId(null);
        return bookRepository.save(book);
    }

    @Transactional
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id);
    }

    @Transactional
    public Book update(Book book) {
        Optional<Book> bookOptional = bookRepository.findById(book.getId());
        if (bookOptional.isEmpty()) {
            throw new BookNotFoundException(book.getId());
        }
        Book updatedBook = bookOptional.get();
        updatedBook.setAuthor(book.getAuthor());
        updatedBook.setTitle(book.getTitle());
        updatedBook.setPublicationYear(book.getPublicationYear());
        updatedBook.setIsbn(book.getIsbn());
        return bookRepository.save(updatedBook);
    }

    static final Counter requests = Counter.build()
            .name("all_book_requests")
            .help("Total number of requests of all books.")
            .register();

    @Transactional(readOnly = true)
    public List<Book> getBooks() {
        requests.inc();
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Book getBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.orElseThrow(() -> new BookNotFoundException(id));
    }
}
