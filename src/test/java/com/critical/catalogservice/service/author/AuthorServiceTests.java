package com.critical.catalogservice.service.author;

import com.critical.catalogservice.data.entity.Author;
import com.critical.catalogservice.data.repository.AuthorRepository;
import com.critical.catalogservice.dtos.AuthorDto;
import com.critical.catalogservice.util.exception.EntityNullException;
import com.critical.catalogservice.util.exception.SaveEntityException;
import jakarta.persistence.EntityNotFoundException;
import nl.altindag.log.LogCaptor;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthorServiceTests {

    private static LogCaptor logCaptor;

    private AuthorRepository repository;

    private AuthorService service;

    @BeforeEach
    void setUp() {

        logCaptor = LogCaptor.forClass(AuthorService.class);
        repository = mock(AuthorRepository.class);
        service = new AuthorService(this.repository);
    }

    @Test
    public void givenValidAuthorObject_whenSave_thenReturnSaveAuthorId() {
        // Arrange
        var author = Instancio.create(Author.class);
        var authorDto = Instancio.create(AuthorDto.class);
        when(this.repository.save(any(Author.class))).thenReturn(author);
        // Act
        var result = this.service.createAuthor(authorDto);
        // Assert
        Assertions.assertEquals(author.getId(), result);
    }

    @Test
    public void givenValidAuthorObject_whenErrorOccursWhileSaving_thenThrowException() {
        // Arrange
        var authorDto = Instancio.create(AuthorDto.class);
        var errorMessage = "Error while author address";
        when(this.repository.save(any(Author.class))).thenThrow(new SaveEntityException(errorMessage));
        // Act
        Exception exception = assertThrows(SaveEntityException.class, () -> {
            this.service.createAuthor(authorDto);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenInvalidAuthorObject_whenSaving_thenThrowException() {
        // Arrange
        var errorMessage = "Author received is null";
        when(this.repository.save(any(Author.class))).thenThrow(new SaveEntityException(errorMessage));
        // Act
        Exception exception = assertThrows(EntityNullException.class, () -> {
            this.service.createAuthor(null);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenNoAuthorExists_whenGettingAllAuthors_thenReturnsEmptyList() {
        // Arrange
        when(this.repository.findAll()).thenReturn(new ArrayList<>());
        // Act
        var result = service.getAllAuthors();
        // Assert
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void givenAuthorExists_whenGettingAllAuthors_thenReturnsAuthors() {
        // Arrange
        var Authors = Instancio.ofList(Author.class).size(10).create();
        when(this.repository.findAll()).thenReturn(Authors);
        // Act
        var result = service.getAllAuthors();
        // Assert
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(Authors.size(), result.size());
        for (var i = 0; i < Authors.size(); i++) {
            Assertions.assertEquals(Authors.get(i).getName(), result.get(i).name);
        }
    }

    @Test
    public void givenValidAuthorId_whenGettingAuthorById_thenReturnsAuthor() {
        // Arrange
        var AuthorId = 1;
        var author = Instancio.create(Author.class);
        when(this.repository.findById(AuthorId)).thenReturn(Optional.ofNullable(author));
        // Act
        var result = service.getAuthorById(AuthorId);
        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(author.getName(), result.name);
    }

    @Test
    public void givenInvalidAuthorId_whenGettingAuthorById_thenThrowsException() {
        // Arrange
        var AuthorId = 1;
        var errorMessage = "Author not found with the Id: " + AuthorId;
        when(this.repository.findById(AuthorId)).thenReturn(Optional.ofNullable(null));
        // Act
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            service.getAuthorById(AuthorId);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenValidAuthorId_whenDeletingAuthor_thenLogsSuccessMessage() {
        // Arrange
        var AuthorId = 1;
        var expectedMessage = "Author deleted with success.";
        doNothing().when(this.repository).deleteById(AuthorId);
        // Act
        service.deleteAuthor(AuthorId);
        // Assert
        assertThat(logCaptor.getInfoLogs()).containsExactly(expectedMessage);
    }

    @Test
    public void givenInvalidAuthor_whenUpdateAuthor_thenThrowsException() {
        // Arrange
        var AuthorId = 1;
        var errorMessage = "Author received is null";
        // Act
        Exception exception = assertThrows(EntityNullException.class, () -> {
            service.updateAuthor(AuthorId, null);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenAuthorIdThatDoesNot_whenUpdateAuthor_thenThrowsException() {
        // Arrange
        var AuthorId = 1;
        var authorDto = Instancio.create(AuthorDto.class);
        var errorMessage = "Author not found with the Id: " + AuthorId;
        when(this.repository.findById(AuthorId)).thenReturn(Optional.ofNullable(null));
        // Act
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            service.updateAuthor(AuthorId, authorDto);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
        verify(repository, times(1)).findById(AuthorId);
        verify(repository, times(0)).save(any(Author.class));
    }

    @Test
    public void givenValidAuthor_whenErrorOccursWhileUpdating_thenThrowsException() {
        // Arrange
        var AuthorId = 1;
        var authorDto = Instancio.create(AuthorDto.class);
        var author = Instancio.create(Author.class);
        var errorMessage = "Error occurred while Updating Author";
        var expectedMessage = "Error occurred while saving the author information";
        when(this.repository.findById(AuthorId)).thenReturn(Optional.ofNullable(author));
        when(this.repository.save(any(Author.class))).thenThrow(new SaveEntityException(errorMessage));
        // Act
        Exception exception = assertThrows(SaveEntityException.class, () -> {
            service.updateAuthor(AuthorId, authorDto);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
        assertThat(logCaptor.getErrorLogs()).containsExactly(expectedMessage);
        verify(repository, times(1)).findById(AuthorId);
        verify(repository, times(1)).save(any(Author.class));
    }

    @Test
    public void givenValidAuthor_whenUpdating_thenLogsSuccessMessage() {
        // Arrange
        var expectedMessage = "Author saved with success";
        var AuthorId = 1;
        var authorDto = Instancio.create(AuthorDto.class);
        var author = Instancio.create(Author.class);
        when(this.repository.findById(AuthorId)).thenReturn(Optional.ofNullable(author));
        when(this.repository.save(any(Author.class))).thenReturn(author);
        // Act
        service.updateAuthor(AuthorId, authorDto);
        // Assert
        verify(repository, times(1)).findById(AuthorId);
        verify(repository, times(1)).save(any(Author.class));
        assertThat(logCaptor.getInfoLogs()).containsExactly(expectedMessage);
    }
}