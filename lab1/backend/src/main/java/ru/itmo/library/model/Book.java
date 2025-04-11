package ru.itmo.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "title is mandatory")
    private String title;
    @NotEmpty(message = "author is mandatory")
    private String author;
    @NotNull(message = "publicationYear is mandatory")
    private Integer publicationYear;
    @NotEmpty(message = "isbn is mandatory")
    private String isbn;
}
