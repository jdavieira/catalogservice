package com.critical.catalogservice.controller;

import com.critical.catalogservice.dtos.PublisherDto;
import com.critical.catalogservice.dtos.error.ErrorResponse;
import com.critical.catalogservice.service.publisher.PublisherService;
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

public class PublisherControllerTests {

    private PublisherService service;

    private PublisherController controller;

    @BeforeEach
    void setUp() {

        service = mock(PublisherService.class);
        controller = new PublisherController(this.service);
    }

    @Test
    public void givenRequestForPublishers_whenPublishersExist_thenReturnsListOfPublishers() {
        // Arrange
        var publishers = Instancio.ofList(PublisherDto.class).size(10).create();
        when(this.service.getAllPublishers()).thenReturn(publishers);
        // Act
        var result = this.controller.getAllPublishers();
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertFalse(result.getBody().isEmpty());
        assertThat(publishers).hasSameElementsAs(result.getBody());
    }

    @Test
    public void givenPublisherIdToDelete_whenDeletingPublisher_thenReturnNoContent() {
        // Arrange
        var publisherId = 1;
        doNothing().when(this.service).deletePublisher(publisherId);
        // Act
        var result = this.controller.deletePublisher(publisherId);
        // Assert
        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    public void givenPublisherId_whenPublisherExists_thenReturnPublisher() {
        // Arrange
        var publisherId = 1;
        var publisher = Instancio.create(PublisherDto.class);
        when(this.service.getPublisherById(publisherId)).thenReturn(publisher);
        // Act
        var result = this.controller.getPublisherById(publisherId);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        AssertionsForClassTypes.assertThat(publisher).isEqualTo(result.getBody());
    }

    @Test
    public void givenPublisherId_whenPublisherDotNotExists_thenThrowException() {
        // Arrange
        var errorMessage = "Entity not found";
        var publisherId = 1;
        when(this.service.getPublisherById(publisherId)).thenThrow(new EntityNotFoundException(errorMessage));
        // Act
        var result = this.controller.getPublisherById(publisherId);
        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenValidPublisher_whenCreatingPublisher_thenReturnsPublisherId() {
        // Arrange
        var publisherId = 1;
        var publisher = Instancio.create(PublisherDto.class);
        when(this.service.createPublisher(publisher)).thenReturn(publisherId);
        // Act
        var result = this.controller.createPublisher(publisher);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals(publisherId, result.getBody());
    }

    @Test
    public void givenPublisherToCreate_whenErrorOccursWhileCreating_thenThrowException() {
        // Arrange
        var errorMessage = "Error creating entity";
        var publisher = Instancio.create(PublisherDto.class);
        when(this.service.createPublisher(publisher)).thenThrow(new SaveEntityException(errorMessage));
        // Act
        var result = this.controller.createPublisher(publisher);
        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenValidPublisher_whenUpdatingPublisher_thenReturnsOk() {
        // Arrange
        var publisherId = 1;
        var publisher = Instancio.create(PublisherDto.class);
        when(this.service.updatePublisher(publisherId, publisher)).thenReturn(true);
        // Act
        var result = this.controller.updatePublisher(publisherId, publisher);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void givenValidPublisher_whenErrorOccursWhileUpdatingPublisher_thenThrowsException() {
        // Arrange
        var errorMessage = "Error updating entity";
        var publisherId = 1;
        var publisher = Instancio.create(PublisherDto.class);
        doThrow(new SaveEntityException(errorMessage)).when(this.service).updatePublisher(publisherId, publisher);
        // Act
        var result = this.controller.updatePublisher(publisherId, publisher);
        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenPublisherId_whenPublisherIdDoesNotExists_thenThrowsException() {
        // Arrange
        var errorMessage = "Entity not found";
        var publisherId = 1;
        var publisher = Instancio.create(PublisherDto.class);
        doThrow(new EntityNotFoundException(errorMessage)).when(this.service).updatePublisher(publisherId, publisher);
        // Act
        var result = this.controller.updatePublisher(publisherId, publisher);
        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }
}