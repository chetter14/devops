package ru.itmo.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.library.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
