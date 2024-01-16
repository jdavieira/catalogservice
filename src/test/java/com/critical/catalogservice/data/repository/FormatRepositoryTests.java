package com.critical.catalogservice.data.repository;

import com.critical.catalogservice.data.entity.Book;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class FormatRepositoryTests extends BaseRepositoryTests {

    @Autowired
    private FormatRepository repository;

    private Book book;

    @BeforeEach
    public void setUp() {
        // Arrange
        book = Instancio.create(Book.class);
    }

    @Test
    @DisplayName("JUnit test for save Format operation")
    public void givenValidFormatObject_whenSave_thenReturnSaveFormat() {
        // Act
        var result = repository.save(book.getFormats().get(0));
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("JUnit test for get Format List")
    public void givenFormatList_whenFindAll_thenReturnFormatList() {
        // Arrange
        repository.saveAll(book.getFormats());
        // Act
        var formats = repository.findAll();
        // Assert
        assertThat(formats).isNotEmpty();
        assertThat(formats.size()).isEqualTo(book.getFormats().size());
    }

    @Test
    @DisplayName("JUnit test for get Format By Id")
    public void givenFormatObject_whenFindById_thenReturnFormatObject() {
        // Arrange
        var format = book.getFormats().get(0);
        repository.save(format);
        // Act
        var result = repository.findById(format.getId()).get();
        // Assert
        assertThat(result).isNotNull();
    }
}