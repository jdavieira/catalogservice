package com.critical.catalogservice.service.format;

import com.critical.catalogservice.data.entity.Format;
import com.critical.catalogservice.data.repository.FormatRepository;
import com.critical.catalogservice.dtos.FormatDto;
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

public class FormatServiceTests {

    private static LogCaptor logCaptor;

    private FormatRepository repository;

    private FormatService service;

    @BeforeEach
    void setUp() {

        logCaptor = LogCaptor.forClass(FormatService.class);
        repository = mock(FormatRepository.class);
        service = new FormatService(this.repository);
    }

    @Test
    public void givenValidFormatObject_whenSave_thenReturnSaveFormatId() {
        // Arrange
        var format = Instancio.create(Format.class);
        var formatDto = Instancio.create(FormatDto.class);
        when(this.repository.save(any(Format.class))).thenReturn(format);
        // Act
        var result = this.service.createFormat(formatDto);
        // Assert
        Assertions.assertEquals(format.getId(), result);
    }

    @Test
    public void givenValidFormatObject_whenErrorOccursWhileSaving_thenThrowException() {
        // Arrange
        var formatDto = Instancio.create(FormatDto.class);
        var errorMessage = "Error while format address";
        when(this.repository.save(any(Format.class))).thenThrow(new SaveEntityException(errorMessage));
        // Act
        Exception exception = assertThrows(SaveEntityException.class, () -> {
            this.service.createFormat(formatDto);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenInvalidFormatObject_whenSaving_thenThrowException() {
        // Arrange
        var errorMessage = "Format received is null";
        when(this.repository.save(any(Format.class))).thenThrow(new SaveEntityException(errorMessage));
        // Act
        Exception exception = assertThrows(EntityNullException.class, () -> {
            this.service.createFormat(null);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenNoFormatExists_whenGettingAllFormats_thenReturnsEmptyList() {
        // Arrange
        when(this.repository.findAll()).thenReturn(new ArrayList<>());
        // Act
        var result = service.getAllFormats();
        // Assert
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void givenFormatExists_whenGettingAllFormats_thenReturnsFormats() {
        // Arrange
        var Formats = Instancio.ofList(Format.class).size(10).create();
        when(this.repository.findAll()).thenReturn(Formats);
        // Act
        var result = service.getAllFormats();
        // Assert
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(Formats.size(), result.size());
        for (var i = 0; i < Formats.size(); i++) {
            Assertions.assertEquals(Formats.get(i).getName(), result.get(i).name);
        }
    }

    @Test
    public void givenValidFormatId_whenGettingFormatById_thenReturnsFormat() {
        // Arrange
        var formatId = 1;
        var format = Instancio.create(Format.class);
        when(this.repository.findById(formatId)).thenReturn(Optional.ofNullable(format));
        // Act
        var result = service.getFormatById(formatId);
        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(format.getName(), result.name);
    }

    @Test
    public void givenInvalidFormatId_whenGettingFormatById_thenThrowsException() {
        // Arrange
        var formatId = 1;
        var errorMessage = "Format not found with the Id: " + formatId;
        when(this.repository.findById(formatId)).thenReturn(Optional.ofNullable(null));
        // Act
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            service.getFormatById(formatId);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenValidFormatId_whenDeletingFormat_thenLogsSuccessMessage() {
        // Arrange
        var formatId = 1;
        var expectedMessage = "Format deleted with success.";
        doNothing().when(this.repository).deleteById(formatId);
        // Act
        service.deleteFormat(formatId);
        // Assert
        assertThat(logCaptor.getInfoLogs()).containsExactly(expectedMessage);
    }

    @Test
    public void givenInvalidFormat_whenUpdateFormat_thenThrowsException() {
        // Arrange
        var formatId = 1;
        var errorMessage = "Format received is null";
        // Act
        Exception exception = assertThrows(EntityNullException.class, () -> {
            service.updateFormat(formatId, null);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenFormatIdThatDoesNot_whenUpdateFormat_thenThrowsException() {
        // Arrange
        var formatId = 1;
        var formatDto = Instancio.create(FormatDto.class);
        var errorMessage = "Format not found with the Id: " + formatId;
        when(this.repository.findById(formatId)).thenReturn(Optional.ofNullable(null));
        // Act
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            service.updateFormat(formatId, formatDto);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
        verify(repository, times(1)).findById(formatId);
        verify(repository, times(0)).save(any(Format.class));
    }

    @Test
    public void givenValidFormat_whenErrorOccursWhileUpdating_thenThrowsException() {
        // Arrange
        var formatId = 1;
        var formatDto = Instancio.create(FormatDto.class);
        var format = Instancio.create(Format.class);
        var errorMessage = "Error occurred while Updating Format";
        var expectedMessage = "Error occurred while saving the Format information";
        when(this.repository.findById(formatId)).thenReturn(Optional.ofNullable(format));
        when(this.repository.save(any(Format.class))).thenThrow(new SaveEntityException(errorMessage));
        // Act
        Exception exception = assertThrows(SaveEntityException.class, () -> {
            service.updateFormat(formatId, formatDto);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
        assertThat(logCaptor.getErrorLogs()).containsExactly(expectedMessage);
        verify(repository, times(1)).findById(formatId);
        verify(repository, times(1)).save(any(Format.class));
    }

    @Test
    public void givenValidFormat_whenUpdating_thenLogsSuccessMessage() {
        // Arrange
        var expectedMessage = "Format saved with success";
        var formatId = 1;
        var formatDto = Instancio.create(FormatDto.class);
        var format = Instancio.create(Format.class);
        when(this.repository.findById(formatId)).thenReturn(Optional.ofNullable(format));
        when(this.repository.save(any(Format.class))).thenReturn(format);
        // Act
        service.updateFormat(formatId, formatDto);
        // Assert
        verify(repository, times(1)).findById(formatId);
        verify(repository, times(1)).save(any(Format.class));
        assertThat(logCaptor.getInfoLogs()).containsExactly(expectedMessage);
    }
}