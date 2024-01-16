package com.critical.catalogservice.data.repository;

import com.critical.catalogservice.data.entity.Book;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class LanguageRepositoryTests extends BaseRepositoryTests {

    @Autowired
    private LanguageRepository repository;

    private Book book;

    @BeforeEach
    public void setUp() {
        // Arrange
        book = Instancio.create(Book.class);
    }

    @Test
    @DisplayName("JUnit test for save language operation")
    public void givenValidLanguageObject_whenSave_thenReturnSaveLanguage() {
        // Act
        var result = repository.save(book.getLanguages().get(0));
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("JUnit test for get Language List")
    public void givenLanguageList_whenFindAll_thenReturnLanguageList() {
        // Arrange
        repository.saveAll(book.getLanguages());
        // Act
        var languages = repository.findAll();
        // Assert
        assertThat(languages).isNotEmpty();
        assertThat(languages.size()).isEqualTo(book.getLanguages().size());
    }

    @Test
    @DisplayName("JUnit test for get Language By Id")
    public void givenLanguageObject_whenFindById_thenReturnLanguageObject() {
        // Arrange
        var language = book.getLanguages().get(0);
        repository.save(language);
        // Act
        var result = repository.findById(language.getId()).get();
        // Assert
        assertThat(result).isNotNull();
    }
}