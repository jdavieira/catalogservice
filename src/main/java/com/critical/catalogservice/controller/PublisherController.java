package com.critical.catalogservice.controller;

import com.critical.catalogservice.dtos.PublisherDto;
import com.critical.catalogservice.dtos.error.ErrorResponse;
import com.critical.catalogservice.service.publisher.PublisherService;
import com.critical.catalogservice.util.exception.EntityNullException;
import com.critical.catalogservice.util.exception.SaveEntityDataIntegrityViolationException;
import com.critical.catalogservice.util.exception.SaveEntityException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Publisher", description = "Publisher management API")
@RestController
@RequestMapping("/v1/api")
public class PublisherController {

    private static final Logger logger = LoggerFactory.getLogger(PublisherController.class);

    private final PublisherService publisherService;

    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @Operation(summary = "Retrieve a Publisher by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = PublisherDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}) })
    @GetMapping("/publisher/{id}")
    public ResponseEntity getPublisherById(@PathVariable("id") int id) {

        try {
            return ResponseEntity.ok(this.publisherService.getPublisherById(id));
        }catch (EntityNotFoundException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
        }
    }

    @Operation(summary = "Retrieve all Publisher")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(array =
            @ArraySchema(schema = @Schema(implementation = PublisherDto.class)), mediaType = "application/json") }),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})})
    @GetMapping("/publisher")
    public ResponseEntity<List<PublisherDto>> getAllPublishers() {
        return ResponseEntity.ok(this.publisherService.getAllPublishers());
    }

    @Operation(summary = "Create a new Publisher")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}) })
    @PostMapping("/publisher")
    public ResponseEntity createPublisher(@Valid @RequestBody PublisherDto publisher) {
        try {
            return ResponseEntity.ok(this.publisherService.createPublisher(publisher));
        } catch (SaveEntityException | EntityNullException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
        }catch (SaveEntityDataIntegrityViolationException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage()));
        }
    }

    @Operation(summary = "Update a existing Publisher")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}) })
    @PutMapping("/publisher/{id}")
    public ResponseEntity updatePublisher(@PathVariable("id") int id, @Valid @RequestBody PublisherDto publisher) {
        try {
            return ResponseEntity.ok(this.publisherService.updatePublisher(id, publisher));
        } catch (SaveEntityException | EntityNullException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
        }catch (EntityNotFoundException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
        }
    }

    @Operation(summary = "Delete a existing Publisher")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}) })
    @DeleteMapping("/publisher/{id}")
    public ResponseEntity deletePublisher(@PathVariable("id") int id) {
        try {
            this.publisherService.deletePublisher(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
        }
    }
}