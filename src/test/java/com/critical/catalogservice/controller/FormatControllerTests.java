package com.critical.catalogservice.controller;

import com.critical.catalogservice.dtos.FormatDto;
import com.critical.catalogservice.dtos.error.ErrorResponse;
import com.critical.catalogservice.service.format.FormatService;
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

public class FormatControllerTests {

    private FormatService service;

    private FormatController controller;

    @BeforeEach
    void setUp() {

        service = mock(FormatService.class);
        controller = new FormatController(this.service);
    }

    @Test
    public void givenRequestForFormats_whenFormatsExist_thenReturnsListOfFormats() {
        // Arrange
        var formats = Instancio.ofList(FormatDto.class).size(10).create();
        when(this.service.getAllFormats()).thenReturn(formats);
        // Act
        var result = this.controller.getAllFormats();
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertFalse(result.getBody().isEmpty());
        assertThat(formats).hasSameElementsAs(result.getBody());
    }

    @Test
    public void givenFormatIdToDelete_whenDeletingFormat_thenReturnNoContent() {
        // Arrange
        var formatId = 1;
        doNothing().when(this.service).deleteFormat(formatId);
        // Act
        var result = this.controller.deleteFormat(formatId);
        // Assert
        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    public void givenFormatId_whenFormatExists_thenReturnFormat() {
        // Arrange
        var formatId = 1;
        var format = Instancio.create(FormatDto.class);
        when(this.service.getFormatById(formatId)).thenReturn(format);
        // Act
        var result = this.controller.getFormatById(formatId);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        AssertionsForClassTypes.assertThat(format).isEqualTo(result.getBody());
    }

    @Test
    public void givenFormatId_whenFormatDotNotExists_thenThrowException() {
        // Arrange
        var errorMessage = "Entity not found";
        var formatId = 1;
        when(this.service.getFormatById(formatId)).thenThrow(new EntityNotFoundException(errorMessage));
        // Act
        var result = this.controller.getFormatById(formatId);
        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenValidFormat_whenCreatingFormat_thenReturnsFormatId() {
        // Arrange
        var formatId = 1;
        var format = Instancio.create(FormatDto.class);
        when(this.service.createFormat(format)).thenReturn(formatId);
        // Act
        var result = this.controller.createFormat(format);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals(formatId, result.getBody());
    }

    @Test
    public void givenFormatToCreate_whenErrorOccursWhileCreating_thenThrowException() {
        // Arrange
        var errorMessage = "Error creating entity";
        var format = Instancio.create(FormatDto.class);
        when(this.service.createFormat(format)).thenThrow(new SaveEntityException(errorMessage));
        // Act
        var result = this.controller.createFormat(format);
        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenValidFormat_whenUpdatingFormat_thenReturnsOk() {
        // Arrange
        var formatId = 1;
        var format = Instancio.create(FormatDto.class);
        when(this.service.updateFormat(formatId, format)).thenReturn(true);
        // Act
        var result = this.controller.updateFormat(formatId, format);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void givenValidFormat_whenErrorOccursWhileUpdatingFormat_thenThrowsException() {
        // Arrange
        var errorMessage = "Error updating entity";
        var formatId = 1;
        var format = Instancio.create(FormatDto.class);
        doThrow(new SaveEntityException(errorMessage)).when(this.service).updateFormat(formatId, format);
        // Act
        var result = this.controller.updateFormat(formatId, format);
        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenFormatId_whenFormatIdDoesNotExists_thenThrowsException() {
        // Arrange
        var errorMessage = "Entity not found";
        var formatId = 1;
        var format = Instancio.create(FormatDto.class);
        doThrow(new EntityNotFoundException(errorMessage)).when(this.service).updateFormat(formatId, format);
        // Act
        var result = this.controller.updateFormat(formatId, format);
        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }
}