package ru.itmo.library.controller;

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

    private Counter allBooksRequestCounter;

    @PostConstruct
    public void init() {
        this.allBooksRequestCounter = Counter.builder("all_books_requests")
                .description("Total requests to all books")
                .tag("controller", "BookRestController")
                .register(registry);
    }

    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getBooks() {
        allBooksRequestCounter.increment();
        return bookService.getBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);

    }

    @PostMapping
    public Book createBook(@RequestBody @Valid Book book) {
        return bookService.add(book);
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody @Valid Book book) {
        book.setId(id);
        return bookService.update(book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }
}
