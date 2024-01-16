package com.critical.catalogservice.controller;

import com.critical.catalogservice.dtos.AuthorDto;
import com.critical.catalogservice.dtos.error.ErrorResponse;
import com.critical.catalogservice.service.author.AuthorService;
import com.critical.catalogservice.util.exception.SaveEntityException;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.AssertionsForClassTypes;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AuthorControllerTests {

    private AuthorService service;

    private AuthorController controller;

    @BeforeEach
    void setUp() {

        service = mock(AuthorService.class);
        controller = new AuthorController(this.service);
    }

    @Test
    public void givenRequestForAuthors_whenAuthorsExist_thenReturnsListOfAuthors() {
        // Arrange
        var authors = Instancio.ofList(AuthorDto.class).size(10).create();
        when(this.service.getAllAuthors()).thenReturn(authors);
        // Act
        var result = this.controller.getAllAuthors();
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertFalse(result.getBody().isEmpty());
        assertThat(authors).hasSameElementsAs(result.getBody());
    }

    @Test
    public void givenAuthorIdToDelete_whenDeletingAuthor_thenReturnNoContent() {
        // Arrange
        var authorId = 1;
        doNothing().when(this.service).deleteAuthor(authorId);
        // Act
        var result = this.controller.deleteAuthor(authorId);
        // Assert
        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    public void givenAuthorId_whenAuthorExists_thenReturnAuthor() {
        // Arrange
        var authorId = 1;
        var author = Instancio.create(AuthorDto.class);
        when(this.service.getAuthorById(authorId)).thenReturn(author);
        // Act
        var result = this.controller.getAuthorById(authorId);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        AssertionsForClassTypes.assertThat(author).isEqualTo(result.getBody());
    }

    @Test
    public void givenAuthorId_whenAuthorDotNotExists_thenThrowException() {
        // Arrange
        var errorMessage = "Entity not found";
        var authorId = 1;
        when(this.service.getAuthorById(authorId)).thenThrow(new EntityNotFoundException(errorMessage));
        // Act
        var result = this.controller.getAuthorById(authorId);
        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenValidAuthor_whenCreatingAuthor_thenReturnsAuthorId() {
        // Arrange
        var authorId = 1;
        var author = Instancio.create(AuthorDto.class);
        when(this.service.createAuthor(author)).thenReturn(authorId);
        // Act
        var result = this.controller.createAuthor(author);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals(authorId, result.getBody());
    }

    @Test
    public void givenAuthorToCreate_whenErrorOccursWhileCreating_thenThrowException() {
        // Arrange
        var errorMessage = "Error creating entity";
        var author = Instancio.create(AuthorDto.class);
        when(this.service.createAuthor(author)).thenThrow(new SaveEntityException(errorMessage));
        // Act
        var result = this.controller.createAuthor(author);
        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenValidAuthor_whenUpdatingAuthor_thenReturnsOk() {
        // Arrange
        var authorId = 1;
        var author = Instancio.create(AuthorDto.class);
        when(this.service.updateAuthor(authorId, author)).thenReturn(true);
        // Act
        var result = this.controller.updateAuthor(authorId, author);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void givenValidAuthor_whenErrorOccursWhileUpdatingAuthor_thenThrowsException() {
        // Arrange
        var errorMessage = "Error updating entity";
        var authorId = 1;
        var author = Instancio.create(AuthorDto.class);
        doThrow(new SaveEntityException(errorMessage)).when(this.service).updateAuthor(authorId, author);
        // Act
        var result = this.controller.updateAuthor(authorId, author);
        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenAuthorId_whenAuthorIdDoesNotExists_thenThrowsException() {
        // Arrange
        var errorMessage = "Entity not found";
        var authorId = 1;
        var author = Instancio.create(AuthorDto.class);
        doThrow(new EntityNotFoundException(errorMessage)).when(this.service).updateAuthor(authorId, author);
        // Act
        var result = this.controller.updateAuthor(authorId, author);
        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }
}