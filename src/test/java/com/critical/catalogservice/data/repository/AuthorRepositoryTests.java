package com.critical.catalogservice.data.repository;

import com.critical.catalogservice.data.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
public class AuthorRepositoryTests extends BaseRepositoryTests{
    @Autowired
    private AuthorRepository repository;

    private Book book;

    @BeforeEach
    public void setUp() {
        // Arrange
        book = Instancio.create(Book.class);
    }

    @Test
    @DisplayName("JUnit test for save Author operation")
    public void givenValidAuthorObject_whenSave_thenReturnSaveAuthor() {
        // Act
        var result = repository.save(book.getAuthors().get(0));
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("JUnit test for get Author List")
    public void givenAuthorList_whenFindAll_thenReturnAuthorList() {
        // Arrange
        repository.saveAll(book.getAuthors());
        // Act
        var authors = repository.findAll();
        // Assert
        assertThat(authors).isNotEmpty();
        assertThat(authors.size()).isEqualTo(book.getAuthors().size());
    }

    @Test
    @DisplayName("JUnit test for get Author By Id")
    public void givenAuthorObject_whenFindById_thenReturnAuthorObject() {
        // Arrange
        var author = book.getAuthors().get(0);
        repository.save(author);
        // Act
        var result = repository.findById(author.getId()).get();
        // Assert
        assertThat(result).isNotNull();
    }
}