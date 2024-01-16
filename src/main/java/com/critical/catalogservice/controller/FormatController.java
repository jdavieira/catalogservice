package com.critical.catalogservice.controller;

import com.critical.catalogservice.dtos.FormatDto;
import com.critical.catalogservice.dtos.error.ErrorResponse;
import com.critical.catalogservice.service.format.FormatService;
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

@Tag(name = "Format", description = "Format management API")
@RestController
@RequestMapping("/v1/api")
public class FormatController {
    private static final Logger logger = LoggerFactory.getLogger(FormatController.class);

    private final FormatService formatService;

    @Autowired
    public FormatController(FormatService formatService) {
        this.formatService = formatService;
    }

    @Operation(summary = "Retrieve a Format by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = FormatDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}) })
    @GetMapping("/format/{id}")
    public ResponseEntity getFormatById(@PathVariable("id") int id) {

        try {
            return ResponseEntity.ok(this.formatService.getFormatById(id));
        }catch (EntityNotFoundException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
        }
    }

    @Operation(summary = "Retrieve all Formats")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(array =
            @ArraySchema(schema = @Schema(implementation = FormatDto.class)), mediaType = "application/json") }),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})})
    @GetMapping("/format")
    public ResponseEntity<List<FormatDto>> getAllFormats() {
        return ResponseEntity.ok(this.formatService.getAllFormats());
    }

    @Operation(summary = "Create a new Format")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}) })
    @PostMapping("/format")
    public ResponseEntity createFormat(@Valid @RequestBody FormatDto format) {
        try {
            return ResponseEntity.ok(this.formatService.createFormat(format));
        } catch (SaveEntityException | EntityNullException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
        } catch (SaveEntityDataIntegrityViolationException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage()));
        }
    }

    @Operation(summary = "Update a existing Format")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}) })
    @PutMapping("/format/{id}")
    public ResponseEntity updateFormat(@PathVariable("id") int id, @Valid @RequestBody FormatDto format) {
        try {
            return ResponseEntity.ok(this.formatService.updateFormat(id, format));
        } catch (SaveEntityException | EntityNullException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
        }catch (EntityNotFoundException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
        }
    }

    @Operation(summary = "Delete a existing Format")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "403", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}) })
    @DeleteMapping("/format/{id}")
    public ResponseEntity deleteFormat(@PathVariable("id") int id) {
        try {
            this.formatService.deleteFormat(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException exception){
            logger.warn(exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
        }
    }
}