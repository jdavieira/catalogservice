package com.critical.catalogservice.service.tag;

import com.critical.catalogservice.data.entity.Tag;
import com.critical.catalogservice.data.repository.TagRepository;
import com.critical.catalogservice.dtos.TagDto;
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

public class TagServiceTests {

    private static LogCaptor logCaptor;

    private TagRepository repository;

    private TagService service;

    @BeforeEach
    void setUp() {

        logCaptor = LogCaptor.forClass(TagService.class);
        repository = mock(TagRepository.class);
        service = new TagService(this.repository);
    }

    @Test
    public void givenValidTagObject_whenSave_thenReturnSaveTagId() {
        // Arrange
        var tag = Instancio.create(Tag.class);
        var tagDto = Instancio.create(TagDto.class);
        when(this.repository.save(any(Tag.class))).thenReturn(tag);
        // Act
        var result = this.service.createTag(tagDto);
        // Assert
        Assertions.assertEquals(tag.getId(), result);
    }

    @Test
    public void givenValidTagObject_whenErrorOccursWhileSaving_thenThrowException() {
        // Arrange
        var tagDto = Instancio.create(TagDto.class);
        var errorMessage = "Error while tag address";
        when(this.repository.save(any(Tag.class))).thenThrow(new SaveEntityException(errorMessage));
        // Act
        Exception exception = assertThrows(SaveEntityException.class, () -> {
            this.service.createTag(tagDto);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenInvalidTagObject_whenSaving_thenThrowException() {
        // Arrange
        var errorMessage = "Tag received is null";
        when(this.repository.save(any(Tag.class))).thenThrow(new SaveEntityException(errorMessage));
        // Act
        Exception exception = assertThrows(EntityNullException.class, () -> {
            this.service.createTag(null);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenNoTagExists_whenGettingAllTags_thenReturnsEmptyList() {
        // Arrange
        when(this.repository.findAll()).thenReturn(new ArrayList<>());
        // Act
        var result = service.getAllTags();
        // Assert
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void givenTagExists_whenGettingAllTags_thenReturnsTags() {
        // Arrange
        var tags = Instancio.ofList(Tag.class).size(10).create();
        when(this.repository.findAll()).thenReturn(tags);
        // Act
        var result = service.getAllTags();
        // Assert
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(tags.size(), result.size());
        for (var i = 0; i < tags.size(); i++) {
            Assertions.assertEquals(tags.get(i).getName(), result.get(i).name);
        }
    }

    @Test
    public void givenValidTagId_whenGettingTagById_thenReturnsTag() {
        // Arrange
        var tagId = 1;
        var tag = Instancio.create(Tag.class);
        when(this.repository.findById(tagId)).thenReturn(Optional.ofNullable(tag));
        // Act
        var result = service.getTagById(tagId);
        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(tag.getName(), result.name);
    }

    @Test
    public void givenInvalidTagId_whenGettingTagById_thenThrowsException() {
        // Arrange
        var tagId = 1;
        var errorMessage = "Tag not found with the Id: " + tagId;
        when(this.repository.findById(tagId)).thenReturn(Optional.ofNullable(null));
        // Act
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            service.getTagById(tagId);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenValidTagId_whenDeletingTag_thenLogsSuccessMessage() {
        // Arrange
        var tagId = 1;
        var expectedMessage = "Tag deleted with success.";
        doNothing().when(this.repository).deleteById(tagId);
        // Act
        service.deleteTag(tagId);
        // Assert
        assertThat(logCaptor.getInfoLogs()).containsExactly(expectedMessage);
    }

    @Test
    public void givenInvalidTag_whenUpdateTag_thenThrowsException() {
        // Arrange
        var tagId = 1;
        var errorMessage = "Tag received is null";
        // Act
        Exception exception = assertThrows(EntityNullException.class, () -> {
            service.updateTag(tagId, null);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenTagIdThatDoesNot_whenUpdateTag_thenThrowsException() {
        // Arrange
        var tagId = 1;
        var tagDto = Instancio.create(TagDto.class);
        var errorMessage = "Tag not found with the Id: " + tagId;
        when(this.repository.findById(tagId)).thenReturn(Optional.ofNullable(null));
        // Act
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            service.updateTag(tagId, tagDto);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
        verify(repository, times(1)).findById(tagId);
        verify(repository, times(0)).save(any(Tag.class));
    }

    @Test
    public void givenValidTag_whenErrorOccursWhileUpdating_thenThrowsException() {
        // Arrange
        var tagId = 1;
        var tagDto = Instancio.create(TagDto.class);
        var tag = Instancio.create(Tag.class);
        var errorMessage = "Error occurred while Updating Tag";
        var expectedMessage = "Error occurred while saving the tag information";
        when(this.repository.findById(tagId)).thenReturn(Optional.ofNullable(tag));
        when(this.repository.save(any(Tag.class))).thenThrow(new SaveEntityException(errorMessage));
        // Act
        Exception exception = assertThrows(SaveEntityException.class, () -> {
            service.updateTag(tagId, tagDto);
        });
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
        assertThat(logCaptor.getErrorLogs()).containsExactly(expectedMessage);
        verify(repository, times(1)).findById(tagId);
        verify(repository, times(1)).save(any(Tag.class));
    }

    @Test
    public void givenValidTag_whenUpdating_thenLogsSuccessMessage() {
        // Arrange
        var expectedMessage = "Tag saved with success";
        var tagId = 1;
        var tagDto = Instancio.create(TagDto.class);
        var tag = Instancio.create(Tag.class);
        when(this.repository.findById(tagId)).thenReturn(Optional.ofNullable(tag));
        when(this.repository.save(any(Tag.class))).thenReturn(tag);
        // Act
        service.updateTag(tagId, tagDto);
        // Assert
        verify(repository, times(1)).findById(tagId);
        verify(repository, times(1)).save(any(Tag.class));
        assertThat(logCaptor.getInfoLogs()).containsExactly(expectedMessage);
    }
}