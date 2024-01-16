package com.critical.catalogservice.service.publisher;

import com.critical.catalogservice.data.entity.Publisher;
import com.critical.catalogservice.data.repository.PublisherRepository;
import com.critical.catalogservice.dtos.PublisherDto;
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

public class PublisherServiceTests {

    private static LogCaptor logCaptor;

    private PublisherRepository repository;

    private PublisherService service;

    @BeforeEach
    void setUp() {

        logCaptor = LogCaptor.forClass(PublisherService.class);
        repository = mock(PublisherRepository.class);
        service = new PublisherService(this.repository);
    }

    @Test
    public void givenValidPublisherObject_whenSave_thenReturnSavePublisherId() {
        // Arrange
        var tag = Instancio.create(Publisher.class);
        var publisherDto = Instancio.create(PublisherDto.class);
        when(this.repository.save(any(Publisher.class))).thenReturn(tag);
        // Act
        var result = this.service.createPublisher(publisherDto);
        // Assert
        Assertions.assertEquals(tag.getId(), result);
    }

    @Test
    public void givenValidPublisherObject_whenErrorOccursWhileSaving_thenThrowException() {
        // Arrange
        var publisherDto = Instancio.create(PublisherDto.class);
        var errorMessage = "Error while tag address";
        when(this.repository.save(any(Publisher.class))).thenThrow(new SaveEntityException(errorMessage));
        // Act
        Exception exception = assertThrows(SaveEntityException.class, () -> {
            this.service.createPublisher(publisherDto);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenInvalidPublisherObject_whenSaving_thenThrowException() {
        // Arrange
        var errorMessage = "Publisher received is null";
        when(this.repository.save(any(Publisher.class))).thenThrow(new SaveEntityException(errorMessage));
        // Act
        Exception exception = assertThrows(EntityNullException.class, () -> {
            this.service.createPublisher(null);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenNoPublisherExists_whenGettingAllPublishers_thenReturnsEmptyList() {
        // Arrange
        when(this.repository.findAll()).thenReturn(new ArrayList<>());
        // Act
        var result = service.getAllPublishers();
        // Assert
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void givenPublisherExists_whenGettingAllPublishers_thenReturnsPublishers() {
        // Arrange
        var tags = Instancio.ofList(Publisher.class).size(10).create();
        when(this.repository.findAll()).thenReturn(tags);
        // Act
        var result = service.getAllPublishers();
        // Assert
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(tags.size(), result.size());
        for (var i = 0; i < tags.size(); i++) {
            Assertions.assertEquals(tags.get(i).getName(), result.get(i).name);
        }
    }

    @Test
    public void givenValidPublisherId_whenGettingPublisherById_thenReturnsPublisher() {
        // Arrange
        var publisherId = 1;
        var tag = Instancio.create(Publisher.class);
        when(this.repository.findById(publisherId)).thenReturn(Optional.ofNullable(tag));
        // Act
        var result = service.getPublisherById(publisherId);
        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(tag.getName(), result.name);
    }

    @Test
    public void givenInvalidPublisherId_whenGettingPublisherById_thenThrowsException() {
        // Arrange
        var publisherId = 1;
        var errorMessage = "Publisher not found with the Id: " + publisherId;
        when(this.repository.findById(publisherId)).thenReturn(Optional.ofNullable(null));
        // Act
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            service.getPublisherById(publisherId);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenValidPublisherId_whenDeletingPublisher_thenLogsSuccessMessage() {
        // Arrange
        var publisherId = 1;
        var expectedMessage = "Publisher deleted with success.";
        doNothing().when(this.repository).deleteById(publisherId);
        // Act
        service.deletePublisher(publisherId);
        // Assert
        assertThat(logCaptor.getInfoLogs()).containsExactly(expectedMessage);
    }

    @Test
    public void givenInvalidPublisher_whenUpdatePublisher_thenThrowsException() {
        // Arrange
        var publisherId = 1;
        var errorMessage = "Publisher received is null";
        // Act
        Exception exception = assertThrows(EntityNullException.class, () -> {
            service.updatePublisher(publisherId, null);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenPublisherIdThatDoesNot_whenUpdatePublisher_thenThrowsException() {
        // Arrange
        var publisherId = 1;
        var publisherDto = Instancio.create(PublisherDto.class);
        var errorMessage = "Publisher not found with the Id: " + publisherId;
        when(this.repository.findById(publisherId)).thenReturn(Optional.ofNullable(null));
        // Act
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            service.updatePublisher(publisherId, publisherDto);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
        verify(repository, times(1)).findById(publisherId);
        verify(repository, times(0)).save(any(Publisher.class));
    }

    @Test
    public void givenValidPublisher_whenErrorOccursWhileUpdating_thenThrowsException() {
        // Arrange
        var publisherId = 1;
        var publisherDto = Instancio.create(PublisherDto.class);
        var tag = Instancio.create(Publisher.class);
        var errorMessage = "Error occurred while Updating Publisher";
        var expectedMessage = "Error occurred while saving the publisher information";
        when(this.repository.findById(publisherId)).thenReturn(Optional.ofNullable(tag));
        when(this.repository.save(any(Publisher.class))).thenThrow(new SaveEntityException(errorMessage));
        // Act
        Exception exception = assertThrows(SaveEntityException.class, () -> {
            service.updatePublisher(publisherId, publisherDto);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
        assertThat(logCaptor.getErrorLogs()).containsExactly(expectedMessage);
        verify(repository, times(1)).findById(publisherId);
        verify(repository, times(1)).save(any(Publisher.class));
    }

    @Test
    public void givenValidPublisher_whenUpdating_thenLogsSuccessMessage() {
        // Arrange
        var expectedMessage = "Publisher saved with success";
        var publisherId = 1;
        var publisherDto = Instancio.create(PublisherDto.class);
        var tag = Instancio.create(Publisher.class);
        when(this.repository.findById(publisherId)).thenReturn(Optional.ofNullable(tag));
        when(this.repository.save(any(Publisher.class))).thenReturn(tag);
        // Act
        service.updatePublisher(publisherId, publisherDto);
        // Assert
        verify(repository, times(1)).findById(publisherId);
        verify(repository, times(1)).save(any(Publisher.class));
        assertThat(logCaptor.getInfoLogs()).containsExactly(expectedMessage);
    }
}