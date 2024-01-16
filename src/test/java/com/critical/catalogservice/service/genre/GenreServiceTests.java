package com.critical.catalogservice.service.genre;

import com.critical.catalogservice.data.entity.Genre;
import com.critical.catalogservice.data.repository.GenreRepository;
import com.critical.catalogservice.dtos.GenreDto;
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

public class GenreServiceTests {

    private static LogCaptor logCaptor;

    private GenreRepository repository;

    private GenreService service;

    @BeforeEach
    void setUp() {

        logCaptor = LogCaptor.forClass(GenreService.class);
        repository = mock(GenreRepository.class);
        service = new GenreService(this.repository);
    }

    @Test
    public void givenValidGenreObject_whenSave_thenReturnSaveGenreId() {
        // Arrange
        var genre = Instancio.create(Genre.class);
        var genreDto = Instancio.create(GenreDto.class);
        when(this.repository.save(any(Genre.class))).thenReturn(genre);
        // Act
        var result = this.service.createGenre(genreDto);
        // Assert
        Assertions.assertEquals(genre.getId(), result);
    }

    @Test
    public void givenValidGenreObject_whenErrorOccursWhileSaving_thenThrowException() {
        // Arrange
        var genreDto = Instancio.create(GenreDto.class);
        var errorMessage = "Error while genre address";
        when(this.repository.save(any(Genre.class))).thenThrow(new SaveEntityException(errorMessage));
        // Act
        Exception exception = assertThrows(SaveEntityException.class, () -> {
            this.service.createGenre(genreDto);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenInvalidGenreObject_whenSaving_thenThrowException() {
        // Arrange
        var errorMessage = "Genre received is null";
        when(this.repository.save(any(Genre.class))).thenThrow(new SaveEntityException(errorMessage));
        // Act
        Exception exception = assertThrows(EntityNullException.class, () -> {
            this.service.createGenre(null);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenNoGenreExists_whenGettingAllGenres_thenReturnsEmptyList() {
        // Arrange
        when(this.repository.findAll()).thenReturn(new ArrayList<>());
        // Act
        var result = service.getAllGenres();
        // Assert
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void givenGenreExists_whenGettingAllGenres_thenReturnsGenres() {
        // Arrange
        var genres = Instancio.ofList(Genre.class).size(10).create();
        when(this.repository.findAll()).thenReturn(genres);
        // Act
        var result = service.getAllGenres();
        // Assert
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(genres.size(), result.size());
        for (var i = 0; i < genres.size(); i++) {
            Assertions.assertEquals(genres.get(i).getName(), result.get(i).name);
        }
    }

    @Test
    public void givenValidGenreId_whenGettingGenreById_thenReturnsGenre() {
        // Arrange
        var genreId = 1;
        var genre = Instancio.create(Genre.class);
        when(this.repository.findById(genreId)).thenReturn(Optional.ofNullable(genre));
        // Act
        var result = service.getGenreById(genreId);
        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(genre.getName(), result.name);
    }

    @Test
    public void givenInvalidGenreId_whenGettingGenreById_thenThrowsException() {
        // Arrange
        var genreId = 1;
        var errorMessage = "Genre not found with the Id: " + genreId;
        when(this.repository.findById(genreId)).thenReturn(Optional.ofNullable(null));
        // Act
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            service.getGenreById(genreId);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenValidGenreId_whenDeletingGenre_thenLogsSuccessMessage() {
        // Arrange
        var genreId = 1;
        var expectedMessage = "Genre deleted with success.";
        doNothing().when(this.repository).deleteById(genreId);
        // Act
        service.deleteGenre(genreId);
        // Assert
        assertThat(logCaptor.getInfoLogs()).containsExactly(expectedMessage);
    }

    @Test
    public void givenInvalidGenre_whenUpdateGenre_thenThrowsException() {
        // Arrange
        var genreId = 1;
        var errorMessage = "Genre received is null";
        // Act
        Exception exception = assertThrows(EntityNullException.class, () -> {
            service.updateGenre(genreId, null);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenGenreIdThatDoesNot_whenUpdateGenre_thenThrowsException() {
        // Arrange
        var genreId = 1;
        var genreDto = Instancio.create(GenreDto.class);
        var errorMessage = "Genre not found with the Id: " + genreId;
        when(this.repository.findById(genreId)).thenReturn(Optional.ofNullable(null));
        // Act
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            service.updateGenre(genreId, genreDto);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
        verify(repository, times(1)).findById(genreId);
        verify(repository, times(0)).save(any(Genre.class));
    }

    @Test
    public void givenValidGenre_whenErrorOccursWhileUpdating_thenThrowsException() {
        // Arrange
        var genreId = 1;
        var genreDto = Instancio.create(GenreDto.class);
        var genre = Instancio.create(Genre.class);
        var errorMessage = "Error occurred while Updating Genre";
        var expectedMessage = "Error occurred while saving the Genre information";
        when(this.repository.findById(genreId)).thenReturn(Optional.ofNullable(genre));
        when(this.repository.save(any(Genre.class))).thenThrow(new SaveEntityException(errorMessage));
        // Act
        Exception exception = assertThrows(SaveEntityException.class, () -> {
            service.updateGenre(genreId, genreDto);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
        assertThat(logCaptor.getErrorLogs()).containsExactly(expectedMessage);
        verify(repository, times(1)).findById(genreId);
        verify(repository, times(1)).save(any(Genre.class));
    }

    @Test
    public void givenValidGenre_whenUpdating_thenLogsSuccessMessage() {
        // Arrange
        var expectedMessage = "Genre saved with success";
        var genreId = 1;
        var genreDto = Instancio.create(GenreDto.class);
        var genre = Instancio.create(Genre.class);
        when(this.repository.findById(genreId)).thenReturn(Optional.ofNullable(genre));
        when(this.repository.save(any(Genre.class))).thenReturn(genre);
        // Act
        service.updateGenre(genreId, genreDto);
        // Assert
        verify(repository, times(1)).findById(genreId);
        verify(repository, times(1)).save(any(Genre.class));
        assertThat(logCaptor.getInfoLogs()).containsExactly(expectedMessage);
    }
}