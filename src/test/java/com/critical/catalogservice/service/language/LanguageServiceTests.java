package com.critical.catalogservice.service.language;

import com.critical.catalogservice.data.entity.Language;
import com.critical.catalogservice.data.repository.LanguageRepository;
import com.critical.catalogservice.dtos.LanguageDto;
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

public class LanguageServiceTests {

    private static LogCaptor logCaptor;

    private LanguageRepository repository;

    private LanguageService service;

    @BeforeEach
    void setUp() {

        logCaptor = LogCaptor.forClass(LanguageService.class);
        repository = mock(LanguageRepository.class);
        service = new LanguageService(this.repository);
    }

    @Test
    public void givenValidLanguageObject_whenSave_thenReturnSaveLanguageId() {
        // Arrange
        var language = Instancio.create(Language.class);
        var languageDto = Instancio.create(LanguageDto.class);
        when(this.repository.save(any(Language.class))).thenReturn(language);
        // Act
        var result = this.service.createLanguage(languageDto);
        // Assert
        Assertions.assertEquals(language.getId(), result);
    }

    @Test
    public void givenValidLanguageObject_whenErrorOccursWhileSaving_thenThrowException() {
        // Arrange
        var languageDto = Instancio.create(LanguageDto.class);
        var errorMessage = "Error while getting language";
        when(this.repository.save(any(Language.class))).thenThrow(new SaveEntityException(errorMessage));
        // Act
        Exception exception = assertThrows(SaveEntityException.class, () -> {
            this.service.createLanguage(languageDto);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenInvalidLanguageObject_whenSaving_thenThrowException() {
        // Arrange
        var errorMessage = "Language received is null";
        when(this.repository.save(any(Language.class))).thenThrow(new SaveEntityException(errorMessage));
        // Act
        Exception exception = assertThrows(EntityNullException.class, () -> {
            this.service.createLanguage(null);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenNoLanguageExists_whenGettingAllLanguages_thenReturnsEmptyList() {
        // Arrange
        when(this.repository.findAll()).thenReturn(new ArrayList<>());
        // Act
        var result = service.getAllLanguages();
        // Assert
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void givenLanguageExists_whenGettingAllLanguages_thenReturnsLanguages() {
        // Arrange
        var languages = Instancio.ofList(Language.class).size(10).create();
        when(this.repository.findAll()).thenReturn(languages);
        // Act
        var result = service.getAllLanguages();
        // Assert
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(languages.size(), result.size());
        for (var i = 0; i < languages.size(); i++) {
            Assertions.assertEquals(languages.get(i).getName(), result.get(i).name);
        }
    }

    @Test
    public void givenValidLanguageId_whenGettingLanguageById_thenReturnsLanguage() {
        // Arrange
        var languageId = 1;
        var language = Instancio.create(Language.class);
        when(this.repository.findById(languageId)).thenReturn(Optional.ofNullable(language));
        // Act
        var result = service.getLanguageById(languageId);
        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(language.getName(), result.name);
    }

    @Test
    public void givenInvalidLanguageId_whenGettingLanguageById_thenThrowsException() {
        // Arrange
        var languageId = 1;
        var errorMessage = "Language not found with the Id: " + languageId;
        when(this.repository.findById(languageId)).thenReturn(Optional.ofNullable(null));
        // Act
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            service.getLanguageById(languageId);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenValidLanguageId_whenDeletingLanguage_thenLogsSuccessMessage() {
        // Arrange
        var languageId = 1;
        var expectedMessage = "Language deleted with success.";
        doNothing().when(this.repository).deleteById(languageId);
        // Act
        service.deleteLanguage(languageId);
        // Assert
        assertThat(logCaptor.getInfoLogs()).containsExactly(expectedMessage);
    }

    @Test
    public void givenInvalidLanguage_whenUpdateLanguage_thenThrowsException() {
        // Arrange
        var languageId = 1;
        var errorMessage = "Language received is null";
        // Act
        Exception exception = assertThrows(EntityNullException.class, () -> {
            service.updateLanguage(languageId, null);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenLanguageIdThatDoesNot_whenUpdateLanguage_thenThrowsException() {
        // Arrange
        var languageId = 1;
        var languageDto = Instancio.create(LanguageDto.class);
        var errorMessage = "Language not found with the Id: " + languageId;
        when(this.repository.findById(languageId)).thenReturn(Optional.ofNullable(null));
        // Act
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            service.updateLanguage(languageId, languageDto);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
        verify(repository, times(1)).findById(languageId);
        verify(repository, times(0)).save(any(Language.class));
    }

    @Test
    public void givenValidLanguage_whenErrorOccursWhileUpdating_thenThrowsException() {
        // Arrange
        var languageId = 1;
        var languageDto = Instancio.create(LanguageDto.class);
        var language = Instancio.create(Language.class);
        var errorMessage = "Error occurred while Updating Language";
        var expectedMessage = "Error occurred while upserting the language information";
        when(this.repository.findById(languageId)).thenReturn(Optional.ofNullable(language));
        when(this.repository.save(any(Language.class))).thenThrow(new SaveEntityException(errorMessage));
        // Act
        Exception exception = assertThrows(SaveEntityException.class, () -> {
            service.updateLanguage(languageId, languageDto);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
        assertThat(logCaptor.getErrorLogs()).containsExactly(expectedMessage);
        verify(repository, times(1)).findById(languageId);
        verify(repository, times(1)).save(any(Language.class));
    }

    @Test
    public void givenValidLanguage_whenUpdating_thenLogsSuccessMessage() {
        // Arrange
        var expectedMessage = "Language saved with success.";
        var languageId = 1;
        var languageDto = Instancio.create(LanguageDto.class);
        var language = Instancio.create(Language.class);
        when(this.repository.findById(languageId)).thenReturn(Optional.ofNullable(language));
        when(this.repository.save(any(Language.class))).thenReturn(language);
        // Act
        service.updateLanguage(languageId, languageDto);
        // Assert
        verify(repository, times(1)).findById(languageId);
        verify(repository, times(1)).save(any(Language.class));
        assertThat(logCaptor.getInfoLogs()).containsExactly(expectedMessage);
    }
}