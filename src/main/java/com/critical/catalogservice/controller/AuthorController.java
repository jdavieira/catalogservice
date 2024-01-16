package com.critical.catalogservice.controller;

import com.critical.catalogservice.dtos.AuthorDto;
import com.critical.catalogservice.dtos.error.ErrorResponse;
import com.critical.catalogservice.service.author.AuthorService;
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

@Tag(name = "Author", description = "Author management API")
@RestController
@RequestMapping("/v1/api")
public class AuthorController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Operation(summary = "Retrieve a Author by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = AuthorDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}) })
    @GetMapping("/author/{id}")
    public ResponseEntity getAuthorById(@PathVariable("id") int id) {

        try {
            return ResponseEntity.ok(this.authorService.getAuthorById(id));
        }catch(EntityNotFoundException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
        }
    }

    @Operation(summary = "Retrieve all Authors")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(array =
            @ArraySchema(schema = @Schema(implementation = AuthorDto.class)), mediaType = "application/json") }),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})})
    @GetMapping("/author")
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        return ResponseEntity.ok(this.authorService.getAllAuthors());
    }

    @Operation(summary = "Create a new Author")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}) })
    @PostMapping("/author")
    public ResponseEntity createAuthor(@Valid @RequestBody AuthorDto author) {
        try {
            return ResponseEntity.ok(this.authorService.createAuthor(author));
        }catch (SaveEntityException | EntityNullException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
        }catch (SaveEntityDataIntegrityViolationException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage()));
        }
    }

    @Operation(summary = "Update a existing Author")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}) })
    @PutMapping("/author/{id}")
    public ResponseEntity updateAuthor(@PathVariable("id") int id, @Valid @RequestBody AuthorDto author) {
        try {
            return ResponseEntity.ok(this.authorService.updateAuthor(id, author));
        }catch (SaveEntityException | EntityNullException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
        }catch (EntityNotFoundException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
        }
    }

    @Operation(summary = "Delete a existing Author")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}) })
    @DeleteMapping("/author/{id}")
    public ResponseEntity deleteAuthor(@PathVariable("id") int id) {
        try {
            this.authorService.deleteAuthor(id);
            return ResponseEntity.noContent().build();
        } catch ( EntityNotFoundException exception) {
            logger.warn(exception.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
        }
    }
}