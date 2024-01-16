package com.critical.catalogservice.controller;

import com.critical.catalogservice.dtos.book.BookAvailabilityDto;
import com.critical.catalogservice.dtos.book.BookDto;
import com.critical.catalogservice.dtos.book.BookRequestDto;
import com.critical.catalogservice.dtos.book.BookUpdateRequestDto;
import com.critical.catalogservice.dtos.error.ErrorResponse;
import com.critical.catalogservice.service.book.BookService;
import com.critical.catalogservice.util.exception.SaveEntityException;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.AssertionsForClassTypes;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class BookControllerTests {



    private BookService service;

    private BookController controller;

    @BeforeEach
    void setUp() {


        service = mock(BookService.class);
        controller = new BookController(this.service);
    }

    @Test
    public void givenRequestForBooks_whenBooksExist_thenReturnsListOfBooks() {
        // Arrange
        var books = Instancio.ofList(BookDto.class).size(10).create();
        when(this.service.getAllBooks()).thenReturn(books);
        // Act
        var result = this.controller.getAllBooks();
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertFalse(result.getBody().isEmpty());
        assertThat(books).hasSameElementsAs(result.getBody());
    }

    @Test
    public void givenRequestForBooks_whenBooksWithStockExistExist_thenReturnsListOfBooks() {
        // Arrange
        var books = Instancio.ofList(BookDto.class).size(10).create();
        when(this.service.getAllAvailableBooks()).thenReturn(books);
        // Act
        var result = this.controller.getAllAvailableBooks();
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertFalse(result.getBody().isEmpty());
        assertThat(books).hasSameElementsAs(result.getBody());
    }

    @Test
    public void givenBookIdToDelete_whenDeletingBook_thenReturnNoContent() {
        // Arrange
        var bookId = 1;
        doNothing().when(this.service).deleteBook(bookId);
        // Act
        var result = this.controller.deleteBook(bookId);
        // Assert
        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    public void givenBookId_whenBookExists_thenReturnBook() {
        // Arrange
        var bookId = 1;
        var book = Instancio.create(BookDto.class);
        when(this.service.getBookById(bookId)).thenReturn(book);
        // Act
        var result = this.controller.getBookById(bookId);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        AssertionsForClassTypes.assertThat(book).isEqualTo(result.getBody());
    }

    @Test
    public void givenBookId_whenBookDotNotExists_thenThrowException() {
        // Arrange
        var errorMessage = "Entity not found";
        var bookId = 1;
        when(this.service.getBookById(bookId)).thenThrow(new EntityNotFoundException(errorMessage));
        // Act
        var result = this.controller.getBookById(bookId);
        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenValidBook_whenCreatingBook_thenReturnsBookId() {
        // Arrange
        var bookId = 1;
        var book = Instancio.create(BookRequestDto.class);
        when(this.service.createBook(book)).thenReturn(bookId);
        // Act
        var result = this.controller.createBook(book);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals(bookId, result.getBody());
    }

    @Test
    public void givenBookToCreate_whenErrorOccursWhileCreating_thenThrowException() {
        // Arrange
        var errorMessage = "Error creating entity";
        var book = Instancio.create(BookRequestDto.class);
        when(this.service.createBook(book)).thenThrow(new SaveEntityException(errorMessage));
        // Act
        var result = this.controller.createBook(book);
        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenValidBook_whenUpdatingBook_thenReturnsOk() {
        // Arrange
        var bookId = 1;
        var book = Instancio.create(BookUpdateRequestDto.class);
        when(this.service.updateBook(bookId, book)).thenReturn(true);
        // Act
        var result = this.controller.updateBook(bookId, book);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void givenValidBook_whenErrorOccursWhileUpdatingBook_thenThrowsException() {
        // Arrange
        var errorMessage = "Error updating entity";
        var bookId = 1;
        var book = Instancio.create(BookUpdateRequestDto.class);
        doThrow(new SaveEntityException(errorMessage)).when(this.service).updateBook(bookId, book);
        // Act
        var result = this.controller.updateBook(bookId, book);
        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenBookId_whenBookIdDoesNotExists_thenThrowsException() {
        // Arrange
        var errorMessage = "Entity not found";
        var bookId = 1;
        var book = Instancio.create(BookUpdateRequestDto.class);
        doThrow(new EntityNotFoundException(errorMessage)).when(this.service).updateBook(bookId, book);
        // Act
        var result = this.controller.updateBook(bookId, book);
        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenBookOriginalTitle_whenBookExists_thenReturnBook() {
        // Arrange
        var bookOriginalTitle = "original title";
        var books = Instancio.ofList(BookDto.class).size(10).create();
        when(this.service.getBookByOriginalTitle(bookOriginalTitle)).thenReturn(books);
        // Act
        var result = this.controller.getBookByOriginalTitle(bookOriginalTitle);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        AssertionsForClassTypes.assertThat(books).isEqualTo(result.getBody());
    }

    @Test
    public void givenBookOriginalTitle_whenBookDotNotExists_thenThrowException() {
        // Arrange
        var errorMessage = "Entity not found";
        var bookOriginalTitle = "original title";
        when(this.service.getBookByOriginalTitle(bookOriginalTitle)).thenThrow(new EntityNotFoundException(errorMessage));
        // Act
        var result = this.controller.getBookByOriginalTitle(bookOriginalTitle);
        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenBookTitle_whenBookExists_thenReturnBook() {
        // Arrange
        var bookTitle = "title";
        var books = Instancio.ofList(BookDto.class).size(10).create();
        when(this.service.getBookByTitle(bookTitle)).thenReturn(books);
        // Act
        var result = this.controller.getBookByTitle(bookTitle);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        AssertionsForClassTypes.assertThat(books).isEqualTo(result.getBody());
    }

    @Test
    public void givenBookTitle_whenBookDotNotExists_thenThrowException() {
        // Arrange
        var errorMessage = "Entity not found";
        var bookTitle = "title";
        when(this.service.getBookByTitle(bookTitle)).thenThrow(new EntityNotFoundException(errorMessage));
        // Act
        var result = this.controller.getBookByTitle(bookTitle);
        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenBookIsbn_whenBookExists_thenReturnBook() {
        // Arrange
        var bookIsbn = "Isbn";
        var book = Instancio.create(BookDto.class);
        when(this.service.getBookByISBN(bookIsbn)).thenReturn(book);
        // Act
        var result = this.controller.getBookByISBN(bookIsbn);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        AssertionsForClassTypes.assertThat(book).isEqualTo(result.getBody());
    }

    @Test
    public void givenBookIsbn_whenBookDotNotExists_thenThrowException() {
        // Arrange
        var errorMessage = "Entity not found";
        var bookIsbn = "Isbn";
        when(this.service.getBookByISBN(bookIsbn)).thenThrow(new EntityNotFoundException(errorMessage));
        // Act
        var result = this.controller.getBookByISBN(bookIsbn);
        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenBookSynopsis_whenBookExists_thenReturnBook() {
        // Arrange
        var bookSynopsis = "synopsis";
        var books = Instancio.ofList(BookDto.class).size(10).create();
        when(this.service.getBookBySynopsis(bookSynopsis)).thenReturn(books);
        // Act
        var result = this.controller.getBookBySynopsis(bookSynopsis);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        AssertionsForClassTypes.assertThat(books).isEqualTo(result.getBody());
    }

    @Test
    public void givenBookSynopsis_whenBookDotNotExists_thenThrowException() {
        // Arrange
        var errorMessage = "Entity not found";
        var bookSynopsis = "synopsis";
        when(this.service.getBookBySynopsis(bookSynopsis)).thenThrow(new EntityNotFoundException(errorMessage));
        // Act
        var result = this.controller.getBookBySynopsis(bookSynopsis);
        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }

    @Test
    public void givenSearchBook_whenBookExists_thenReturnBooks() {
        // Arrange
        var books = Instancio.ofList(BookDto.class).size(10).create();
        var optionalString = Optional.ofNullable("string");
        var optionalFloat = Optional.ofNullable(1.0);
        var optionalBool = Optional.ofNullable(false);
        var optionalBookAvailabilityDto = Optional.ofNullable(BookAvailabilityDto.AVAILABLE);
        when(this.service.searchBooks(optionalString, optionalString, optionalString, optionalString, optionalBool, optionalFloat, optionalFloat, optionalBool, optionalBookAvailabilityDto)).thenReturn(books);
        // Act
        var result = this.controller.searchBooks(optionalString, optionalString, optionalString, optionalString, optionalBool, optionalFloat, optionalFloat, optionalBool, optionalBookAvailabilityDto);
        // Assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        AssertionsForClassTypes.assertThat(books).isEqualTo(result.getBody());
    }

    @Test
    public void givenSearchBook_whenBookDotNotExists_thenThrowException() {
        // Arrange
        var errorMessage = "Entity not found";
        var optionalString = Optional.ofNullable("string");
        var optionalFloat = Optional.ofNullable(1.0);
        var optionalBool = Optional.ofNullable(false);
        var optionalBookAvailabilityDto = Optional.ofNullable(BookAvailabilityDto.AVAILABLE);
        when(this.service.searchBooks(optionalString, optionalString, optionalString, optionalString, optionalBool, optionalFloat, optionalFloat, optionalBool, optionalBookAvailabilityDto)).thenThrow(new EntityNotFoundException(errorMessage));
        // Act
        var result = this.controller.searchBooks(optionalString, optionalString, optionalString, optionalString, optionalBool, optionalFloat, optionalFloat, optionalBool, optionalBookAvailabilityDto);
        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        var response = (ErrorResponse) result.getBody();
        Assertions.assertEquals(errorMessage, response.description);
    }
}