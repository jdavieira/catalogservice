package com.critical.catalogservice.data.repository;

import com.critical.catalogservice.data.entity.Book;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class GenreRepositoryTests extends BaseRepositoryTests {

    @Autowired
    private GenreRepository repository;

    private Book book;

    @BeforeEach
    public void setUp() {
        // Arrange
        book = Instancio.create(Book.class);
    }

    @Test
    @DisplayName("JUnit test for save Genre operation")
    public void givenValidGenreObject_whenSave_thenReturnSaveGenre() {
        // Act
        var result = repository.save(book.getGenres().get(0));
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("JUnit test for get Genre List")
    public void givenGenreList_whenFindAll_thenReturnGenreList() {
        // Arrange
        repository.saveAll(book.getGenres());
        // Act
        var genres = repository.findAll();
        // Assert
        assertThat(genres).isNotEmpty();
        assertThat(genres.size()).isEqualTo(book.getGenres().size());
    }

    @Test
    @DisplayName("JUnit test for get Genre By Id")
    public void givenGenreObject_whenFindById_thenReturnGenreObject() {
        // Arrange
        var genre = book.getGenres().get(0);
        repository.save(genre);
        // Act
        var result = repository.findById(genre.getId()).get();
        // Assert
        assertThat(result).isNotNull();
    }
}