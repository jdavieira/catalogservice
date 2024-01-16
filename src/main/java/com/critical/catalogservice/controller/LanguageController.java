package com.critical.catalogservice.controller;

import com.critical.catalogservice.dtos.LanguageDto;
import  com.critical.catalogservice.dtos.error.ErrorResponse;
import com.critical.catalogservice.service.language.LanguageService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Language", description = "Language management API")
@RestController
@RequestMapping("/v1/api")
public class LanguageController {
    private static final Logger logger = LoggerFactory.getLogger(LanguageController.class);

    private final LanguageService languageService;

    @Autowired
    public LanguageController(LanguageService languageService) {

        this.languageService = languageService;
    }

    @Operation(summary = "Retrieve a Language by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = LanguageDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}) })
    @GetMapping("/language/{id}")
    public ResponseEntity getLanguageById(@PathVariable("id") int id) {

        try {
            return ResponseEntity.ok(this.languageService.getLanguageById(id));
        }catch (EntityNotFoundException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
        }
    }

    @Operation(summary = "Retrieve all Languages")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(array =
                @ArraySchema(schema = @Schema(implementation = LanguageDto.class)), mediaType = "application/json") }),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})})
    @GetMapping("/language")
    public ResponseEntity<List<LanguageDto>> getAllLanguages() {
        return ResponseEntity.ok(this.languageService.getAllLanguages());
    }

    @Operation(summary = "Create a new Language")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}) })
    @PostMapping("/language")
    public ResponseEntity createLanguage(@Valid @RequestBody LanguageDto language) {
        try {
            return ResponseEntity.ok(this.languageService.createLanguage(language));
        } catch (SaveEntityException | EntityNullException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
        } catch (SaveEntityDataIntegrityViolationException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage()));
        }
    }

    @Operation(summary = "Update a existing Language")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}) })
    @PutMapping("/language/{id}")
    public ResponseEntity updateLanguage(@PathVariable("id") int id, @Valid @RequestBody LanguageDto language) {
        try {
            return ResponseEntity.ok(this.languageService.updateLanguage(id, language));
        } catch (SaveEntityException | EntityNullException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
        }catch (EntityNotFoundException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
        }
    }

    @Operation(summary = "Delete a existing Language")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}) })
    @DeleteMapping("/language/{id}")
    public ResponseEntity deleteLanguage(@PathVariable("id") int id) {
        try {
            this.languageService.deleteLanguage(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
        }
    }
}