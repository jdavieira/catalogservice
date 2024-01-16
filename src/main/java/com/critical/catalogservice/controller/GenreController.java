package com.critical.catalogservice.controller;

import com.critical.catalogservice.dtos.GenreDto;
import com.critical.catalogservice.dtos.error.ErrorResponse;
import com.critical.catalogservice.service.genre.GenreService;
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

@Tag(name = "Genre", description = "Genre management API")
@RestController
@RequestMapping("/v1/api")
public class GenreController {
    private static final Logger logger = LoggerFactory.getLogger(GenreController.class);

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @Operation(summary = "Retrieve a Genre by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = GenreDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}) })
    @GetMapping("/genre/{id}")
    public ResponseEntity getGenreById(@PathVariable("id") int id) {

        try {
            return ResponseEntity.ok(this.genreService.getGenreById(id));
        }catch (EntityNotFoundException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
        }
    }

    @Operation(summary = "Retrieve all Genres")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(array =
            @ArraySchema(schema = @Schema(implementation = GenreDto.class)), mediaType = "application/json") }),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})})
    @GetMapping("/genre")
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        return ResponseEntity.ok(this.genreService.getAllGenres());
    }

    @Operation(summary = "Create a new Genre")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}) })
    @PostMapping("/genre")
    public ResponseEntity createGenre(@Valid @RequestBody GenreDto genre) {
        try {
            return ResponseEntity.ok(this.genreService.createGenre(genre));
        }catch (SaveEntityException | EntityNullException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
        }catch (SaveEntityDataIntegrityViolationException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage()));
        }
    }

    @Operation(summary = "Update a existing Genre")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}) })
    @PutMapping("/genre/{id}")
    public ResponseEntity updateGenre(@PathVariable("id") int id, @Valid @RequestBody GenreDto genre) {
        try {
            return ResponseEntity.ok(this.genreService.updateGenre(id, genre));
        } catch (SaveEntityException | EntityNullException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
        }catch (EntityNotFoundException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
        }
    }

    @Operation(summary = "Delete a existing Genre")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}) })
    @DeleteMapping("/genre/{id}")
    public ResponseEntity deleteGenre(@PathVariable("id") int id) {
        try {
            this.genreService.deleteGenre(id);
            return  ResponseEntity.noContent().build();
        } catch (EntityNotFoundException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
        }
    }
}