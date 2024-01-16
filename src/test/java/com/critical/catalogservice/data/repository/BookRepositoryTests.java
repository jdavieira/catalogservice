package com.critical.catalogservice.data.repository;

import com.critical.catalogservice.data.entity.*;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


public class BookRepositoryTests extends BaseRepositoryTests{

    @Autowired
    private BookRepository repository;

    private Book book;

    @BeforeEach
    public void setUp() {
        // Arrange
        book = Instancio.create(Book.class);
    }

    @Test
    @DisplayName("JUnit test for save Book operation")
    public void givenValidBookObject_whenSave_thenReturnSaveBook() {
        // Act
        var result = repository.save(book);
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("JUnit test for get Book List")
    public void givenBookList_whenFindAll_thenReturnBookList() {
        // Arrange
        repository.save(Instancio.create(Book.class));
        repository.save(Instancio.create(Book.class));
        // Act
        var books = repository.findAll();
        // Assert
        assertThat(books).isNotEmpty();
        assertThat(books.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("JUnit test for get Book By Id")
    public void givenBookObject_whenFindById_thenReturnBookObject() {
        // Arrange
        repository.save(book);
        // Act
        var result = repository.findById(book.getId()).get();
        // Assert
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for get Book By ISBN")
    public void givenBookObject_whenFindByIsbn_thenReturnBookObject() {
        // Arrange
        repository.save(book);
        // Act
        var result = repository.findByIsbn(book.getIsbn());
        // Assert
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for get Book By Original title")
    public void givenBookObject_whenFindByOriginalTitle_thenReturnBookObject() {
        // Arrange
        repository.save(book);
        // Act
        var result = repository.findByOriginalTitle(book.getOriginalTitle());
        // Assert
        assertThat(result).isNotNull();
    }
}