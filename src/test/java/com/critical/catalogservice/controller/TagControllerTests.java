package com.critical.catalogservice.controller;

import com.critical.catalogservice.dtos.TagDto;
import com.critical.catalogservice.dtos.error.ErrorResponse;
import com.critical.catalogservice.service.tag.TagService;
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

public class TagControllerTests {

    private TagService service;

    private TagController controller;

    @BeforeEach
    void setUp() {

        service = mock(TagService.class);
        controller = new TagController(this.service);
    }

    @Test
    public void givenRequestForTags_whenTagsExist_thenReturnsListOfTags() {
        // Arrange
        var tags = Instancio.ofList(TagDto.class).size(10).create();
        when(this.service.getAllTags()).thenReturn(tags);
        // Act
        var result = this.controller.getAllTags();
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertFalse(result.getBody().isEmpty());
        assertThat(tags).hasSameElementsAs(result.getBody());
    }

    @Test
    public void givenTagIdToDelete_whenDeletingTag_thenReturnNoContent() {
        // Arrange
        var tagId = 1;
        doNothing().when(this.service).deleteTag(tagId);
        // Act
        var result = this.controller.deleteTag(tagId);
        // Assert
        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    public void givenTagId_whenTagExists_thenReturnTag() {
        // Arrange
        var tagId = 1;
        var tag = Instancio.create(TagDto.class);
        when(this.service.getTagById(tagId)).thenReturn(tag);
        // Act
        var result = this.controller.getTagById(tagId);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        AssertionsForClassTypes.assertThat(tag).isEqualTo(result.getBody());
    }

    @Test
    public void givenTagId_whenTagDotNotExists_thenThrowException() {
        // Arrange
        var errorMessage = "Entity not found";
        var tagId = 1;
        when(this.service.getTagById(tagId)).thenThrow(new EntityNotFoundException(errorMessage));
        // Act
        var result = this.controller.getTagById(tagId);
        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenValidTag_whenCreatingTag_thenReturnsTagId() {
        // Arrange
        var tagId = 1;
        var tag = Instancio.create(TagDto.class);
        when(this.service.createTag(tag)).thenReturn(tagId);
        // Act
        var result = this.controller.createTag(tag);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals(tagId, result.getBody());
    }

    @Test
    public void givenTagToCreate_whenErrorOccursWhileCreating_thenThrowException() {
        // Arrange
        var errorMessage = "Error creating entity";
        var tag = Instancio.create(TagDto.class);
        when(this.service.createTag(tag)).thenThrow(new SaveEntityException(errorMessage));
        // Act
        var result = this.controller.createTag(tag);
        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenValidTag_whenUpdatingTag_thenReturnsOk() {
        // Arrange
        var tagId = 1;
        var tag = Instancio.create(TagDto.class);
        when(this.service.updateTag(tagId, tag)).thenReturn(true);
        // Act
        var result = this.controller.updateTag(tagId, tag);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void givenValidTag_whenErrorOccursWhileUpdatingTag_thenThrowsException() {
        // Arrange
        var errorMessage = "Error updating entity";
        var tagId = 1;
        var tag = Instancio.create(TagDto.class);
        doThrow(new SaveEntityException(errorMessage)).when(this.service).updateTag(tagId, tag);
        // Act
        var result = this.controller.updateTag(tagId, tag);
        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenTagId_whenTagIdDoesNotExists_thenThrowsException() {
        // Arrange
        var errorMessage = "Entity not found";
        var tagId = 1;
        var tag = Instancio.create(TagDto.class);
        doThrow(new EntityNotFoundException(errorMessage)).when(this.service).updateTag(tagId, tag);
        // Act
        var result = this.controller.updateTag(tagId, tag);
        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }
}