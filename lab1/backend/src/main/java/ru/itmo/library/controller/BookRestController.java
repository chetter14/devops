package ru.itmo.library.controller;

import io.micrometer.core.annotation.Timed;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itmo.library.model.Book;
import ru.itmo.library.repository.BookRepository;
import ru.itmo.library.service.BookService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    private final BookService bookService;

    @Autowired
    private MeterRegistry registry;

    private Counter books_requests_all, books_requests_by_id, books_requests_create,
            books_requests_update, books_requests_delete;

    @PostConstruct
    public void init() {
        this.books_requests_all = Counter.builder("books.requests")
                .tag("operation", "get_all")
                .description("Total requests to get all books")
                .register(registry);

        this.books_requests_by_id = Counter.builder("books.requests")
                .tag("operation", "get_by_id")
                .description("Total requests to get a book by id")
                .register(registry);

        this.books_requests_create = Counter.builder("books.requests")
                .tag("operation", "create")
                .description("Total requests to create a book")
                .register(registry);

        this.books_requests_update = Counter.builder("books.requests")
                .tag("operation", "update")
                .description("Total requests to update a book")
                .register(registry);

        this.books_requests_delete = Counter.builder("books.requests")
                .tag("operation", "delete")
                .description("Total requests to delete a book")
                .register(registry);
    }

    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @Timed(value = "books_get_all_duration", description = "Time taken to get all books")
    @GetMapping
    public List<Book> getBooks() {
        books_requests_all.increment();
        return bookService.getBooks();
    }

    @Timed(value = "books_get_by_id_duration", description = "Time taken to get a book")
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        books_requests_by_id.increment();
        return bookService.getBookById(id);
    }

    @Timed(value = "books_create_duration", description = "Time taken to create a book")
    @PostMapping
    public Book createBook(@RequestBody @Valid Book book) {
        books_requests_create.increment();
        return bookService.add(book);
    }

    @Timed(value = "books_update_duration", description = "Time taken to update a book")
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody @Valid Book book) {
        books_requests_update.increment();
        book.setId(id);
        return bookService.update(book);
    }

    @Timed(value = "books_delete_duration", description = "Time taken to delete a book")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        books_requests_delete.increment();
        bookService.delete(id);
    }
}
