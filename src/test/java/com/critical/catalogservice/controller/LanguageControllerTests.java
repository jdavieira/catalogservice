package com.critical.catalogservice.controller;

import com.critical.catalogservice.dtos.LanguageDto;
import com.critical.catalogservice.dtos.error.ErrorResponse;
import com.critical.catalogservice.service.language.LanguageService;
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

public class LanguageControllerTests {

    private LanguageService service;

    private LanguageController controller;

    @BeforeEach
    void setUp() {

        service = mock(LanguageService.class);
        controller = new LanguageController(this.service);
    }

    @Test
    public void givenRequestForLanguages_whenLanguagesExist_thenReturnsListOfLanguages() {
        // Arrange
        var languages = Instancio.ofList(LanguageDto.class).size(10).create();
        when(this.service.getAllLanguages()).thenReturn(languages);
        // Act
        var result = this.controller.getAllLanguages();
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertFalse(result.getBody().isEmpty());
        assertThat(languages).hasSameElementsAs(result.getBody());
    }

    @Test
    public void givenLanguageIdToDelete_whenDeletingLanguage_thenReturnNoContent() {
        // Arrange
        var languageId = 1;
        doNothing().when(this.service).deleteLanguage(languageId);
        // Act
        var result = this.controller.deleteLanguage(languageId);
        // Assert
        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    public void givenLanguageId_whenLanguageExists_thenReturnLanguage() {
        // Arrange
        var languageId = 1;
        var language = Instancio.create(LanguageDto.class);
        when(this.service.getLanguageById(languageId)).thenReturn(language);
        // Act
        var result = this.controller.getLanguageById(languageId);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        AssertionsForClassTypes.assertThat(language).isEqualTo(result.getBody());
    }

    @Test
    public void givenLanguageId_whenLanguageDotNotExists_thenThrowException() {
        // Arrange
        var errorMessage = "Entity not found";
        var languageId = 1;
        when(this.service.getLanguageById(languageId)).thenThrow(new EntityNotFoundException(errorMessage));
        // Act
        var result = this.controller.getLanguageById(languageId);
        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenValidLanguage_whenCreatingLanguage_thenReturnsLanguageId() {
        // Arrange
        var languageId = 1;
        var language = Instancio.create(LanguageDto.class);
        when(this.service.createLanguage(language)).thenReturn(languageId);
        // Act
        var result = this.controller.createLanguage(language);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals(languageId, result.getBody());
    }

    @Test
    public void givenLanguageToCreate_whenErrorOccursWhileCreating_thenThrowException() {
        // Arrange
        var errorMessage = "Error creating entity";
        var language = Instancio.create(LanguageDto.class);
        when(this.service.createLanguage(language)).thenThrow(new SaveEntityException(errorMessage));
        // Act
        var result = this.controller.createLanguage(language);
        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenValidLanguage_whenUpdatingLanguage_thenReturnsOk() {
        // Arrange
        var languageId = 1;
        var language = Instancio.create(LanguageDto.class);
        when(this.service.updateLanguage(languageId, language)).thenReturn(true);
        // Act
        var result = this.controller.updateLanguage(languageId, language);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void givenValidLanguage_whenErrorOccursWhileUpdatingLanguage_thenThrowsException() {
        // Arrange
        var errorMessage = "Error updating entity";
        var languageId = 1;
        var language = Instancio.create(LanguageDto.class);
        doThrow(new SaveEntityException(errorMessage)).when(this.service).updateLanguage(languageId, language);
        // Act
        var result = this.controller.updateLanguage(languageId, language);
        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenLanguageId_whenLanguageIdDoesNotExists_thenThrowsException() {
        // Arrange
        var errorMessage = "Entity not found";
        var languageId = 1;
        var language = Instancio.create(LanguageDto.class);
        doThrow(new EntityNotFoundException(errorMessage)).when(this.service).updateLanguage(languageId, language);
        // Act
        var result = this.controller.updateLanguage(languageId, language);
        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }
}