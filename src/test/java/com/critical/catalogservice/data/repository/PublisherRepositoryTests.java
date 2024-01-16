package com.critical.catalogservice.data.repository;

import com.critical.catalogservice.data.entity.Book;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


public class PublisherRepositoryTests extends BaseRepositoryTests{
    @Autowired
    private PublisherRepository repository;

    private Book book;

    @BeforeEach
    public void setUp() {
        // Arrange
        book = Instancio.create(Book.class);
    }

    @Test
    @DisplayName("JUnit test for save Publisher operation")
    public void givenValidPublisherObject_whenSave_thenReturnSavePublisher() {
        // Act
        var result = repository.save(book.getPublisher());
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("JUnit test for get Publisher List")
    public void givenPublisherList_whenFindAll_thenReturnPublisherList() {
        // Arrange
        var book2 = Instancio.create(Book.class);
        repository.save(book.getPublisher());
        repository.save(book2.getPublisher());
        // Act
        var publishers = repository.findAll();
        // Assert
        assertThat(publishers).isNotEmpty();
        assertThat(publishers.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("JUnit test for get Publisher By Id")
    public void givenPublisherObject_whenFindById_thenReturnPublisherObject() {
        // Arrange
        var publisher = book.getPublisher();
        repository.save(publisher);
        // Act
        var result = repository.findById(publisher.getId()).get();
        // Assert
        assertThat(result).isNotNull();
    }
}