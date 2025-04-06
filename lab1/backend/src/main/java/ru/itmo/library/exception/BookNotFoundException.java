package ru.itmo.library.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super("Unable to find book with id = " + id);
    }
}
