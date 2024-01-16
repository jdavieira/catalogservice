package com.critical.catalogservice.controller;

import com.critical.catalogservice.dtos.GenreDto;
import com.critical.catalogservice.dtos.error.ErrorResponse;
import com.critical.catalogservice.service.genre.GenreService;
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

public class GenreControllerTests {

    private GenreService service;

    private GenreController controller;

    @BeforeEach
    void setUp() {

        service = mock(GenreService.class);
        controller = new GenreController(this.service);
    }

    @Test
    public void givenRequestForGenres_whenGenresExist_thenReturnsListOfGenres() {
        // Arrange
        var genres = Instancio.ofList(GenreDto.class).size(10).create();
        when(this.service.getAllGenres()).thenReturn(genres);
        // Act
        var result = this.controller.getAllGenres();
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertFalse(result.getBody().isEmpty());
        assertThat(genres).hasSameElementsAs(result.getBody());
    }

    @Test
    public void givenGenreIdToDelete_whenDeletingGenre_thenReturnNoContent() {
        // Arrange
        var genreId = 1;
        doNothing().when(this.service).deleteGenre(genreId);
        // Act
        var result = this.controller.deleteGenre(genreId);
        // Assert
        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    public void givenGenreId_whenGenreExists_thenReturnGenre() {
        // Arrange
        var genreId = 1;
        var genre = Instancio.create(GenreDto.class);
        when(this.service.getGenreById(genreId)).thenReturn(genre);
        // Act
        var result = this.controller.getGenreById(genreId);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        AssertionsForClassTypes.assertThat(genre).isEqualTo(result.getBody());
    }

    @Test
    public void givenGenreId_whenGenreDotNotExists_thenThrowException() {
        // Arrange
        var errorMessage = "Entity not found";
        var genreId = 1;
        when(this.service.getGenreById(genreId)).thenThrow(new EntityNotFoundException(errorMessage));
        // Act
        var result = this.controller.getGenreById(genreId);
        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenValidGenre_whenCreatingGenre_thenReturnsGenreId() {
        // Arrange
        var genreId = 1;
        var genre = Instancio.create(GenreDto.class);
        when(this.service.createGenre(genre)).thenReturn(genreId);
        // Act
        var result = this.controller.createGenre(genre);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals(genreId, result.getBody());
    }

    @Test
    public void givenGenreToCreate_whenErrorOccursWhileCreating_thenThrowException() {
        // Arrange
        var errorMessage = "Error creating entity";
        var genre = Instancio.create(GenreDto.class);
        when(this.service.createGenre(genre)).thenThrow(new SaveEntityException(errorMessage));
        // Act
        var result = this.controller.createGenre(genre);
        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenValidGenre_whenUpdatingGenre_thenReturnsOk() {
        // Arrange
        var genreId = 1;
        var genre = Instancio.create(GenreDto.class);
        when(this.service.updateGenre(genreId, genre)).thenReturn(true);
        // Act
        var result = this.controller.updateGenre(genreId, genre);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void givenValidGenre_whenErrorOccursWhileUpdatingGenre_thenThrowsException() {
        // Arrange
        var errorMessage = "Error updating entity";
        var genreId = 1;
        var genre = Instancio.create(GenreDto.class);
        doThrow(new SaveEntityException(errorMessage)).when(this.service).updateGenre(genreId, genre);
        // Act
        var result = this.controller.updateGenre(genreId, genre);
        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenGenreId_whenGenreIdDoesNotExists_thenThrowsException() {
        // Arrange
        var errorMessage = "Entity not found";
        var genreId = 1;
        var genre = Instancio.create(GenreDto.class);
        doThrow(new EntityNotFoundException(errorMessage)).when(this.service).updateGenre(genreId, genre);
        // Act
        var result = this.controller.updateGenre(genreId, genre);
        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }
}