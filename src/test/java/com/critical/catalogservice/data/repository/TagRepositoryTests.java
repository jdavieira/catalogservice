package com.critical.catalogservice.data.repository;

import com.critical.catalogservice.data.entity.Book;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


public class TagRepositoryTests extends BaseRepositoryTests {
    @Autowired
    private TagRepository repository;

    private Book book;

    @BeforeEach
    public void setUp() {
        // Arrange
        book = Instancio.create(Book.class);
    }

    @Test
    @DisplayName("JUnit test for save tag operation")
    public void givenValidTagObject_whenSave_thenReturnSaveTag() {
        // Act
        var result = repository.save(book.getTags().get(0));
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("JUnit test for get Tag List")
    public void givenTagList_whenFindAll_thenReturnTagList() {
        // Arrange
        repository.saveAll(book.getTags());
        // Act
        var tags = repository.findAll();
        // Assert
        assertThat(tags).isNotEmpty();
        assertThat(tags.size()).isEqualTo(book.getTags().size());
    }

    @Test
    @DisplayName("JUnit test for get Tag By Id")
    public void givenTagObject_whenFindById_thenReturnTagObject() {
        // Arrange
        var tag = book.getTags().get(0);
        repository.save(tag);
        // Act
        var result = repository.findById(tag.getId()).get();
        // Assert
        assertThat(result).isNotNull();
    }
}