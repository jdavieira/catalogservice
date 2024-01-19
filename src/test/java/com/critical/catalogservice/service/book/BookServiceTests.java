package com.critical.catalogservice.service.book;

import com.critical.catalogservice.data.entity.Book;
import com.critical.catalogservice.data.repository.BookRepository;
import com.critical.catalogservice.dtos.book.BookAvailabilityDto;
import com.critical.catalogservice.dtos.book.BookRequestDto;
import com.critical.catalogservice.dtos.book.BookUpdateRequestDto;
import com.critical.catalogservice.service.rabbitMq.BookStockProducer;
import com.critical.catalogservice.util.exception.EntityNullException;
import com.critical.catalogservice.util.exception.SaveEntityException;
import jakarta.persistence.EntityNotFoundException;
import nl.altindag.log.LogCaptor;
import org.instancio.Instancio;
import org.jobrunr.scheduling.JobScheduler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceTests {

    private static LogCaptor logCaptor;

    private BookRepository repository;

    private BookService service;

    @BeforeEach
    void setUp() {

        logCaptor = LogCaptor.forClass(BookService.class);
        repository = mock(BookRepository.class);
        service = new BookService(this.repository,  mock(JobScheduler.class),  mock(BookStockProducer.class));
    }

    @Test
    public void givenValidBookObject_whenSave_thenReturnSaveBookId() {
        // Arrange
        var book = Instancio.create(Book.class);
        var bookDto = Instancio.create(BookRequestDto.class);
        when(this.repository.save(any(Book.class))).thenReturn(book);
        // Act
        var result = this.service.createBook(bookDto);
        // Assert
        Assertions.assertEquals(book.getId(), result);
    }

    @Test
    public void givenValidBookObject_whenErrorOccursWhileSaving_thenThrowException() {
        // Arrange
        var bookDto = Instancio.create(BookRequestDto.class);
        var errorMessage = "Error while getting book";
        when(this.repository.save(any(Book.class))).thenThrow(new SaveEntityException(errorMessage));
        // Act
        Exception exception = assertThrows(SaveEntityException.class, () -> this.service.createBook(bookDto));
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenInvalidBookObject_whenSaving_thenThrowException() {
        // Arrange
        var errorMessage = "Book received is null";
        when(this.repository.save(any(Book.class))).thenThrow(new SaveEntityException(errorMessage));
        // Act
        Exception exception = assertThrows(EntityNullException.class, () -> this.service.createBook(null));
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenNoBookExists_whenGettingAllBooks_thenReturnsEmptyList() {
        // Arrange
        when(this.repository.findAll()).thenReturn(new ArrayList<>());
        // Act
        var result = service.getAllBooks();
        // Assert
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void givenBookExists_whenGettingAllBooks_thenReturnsBooks() {
        // Arrange
        var books = Instancio.ofList(Book.class).size(10).create();
        when(this.repository.findAll()).thenReturn(books);
        // Act
        var result = service.getAllBooks();
        // Assert
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(books.size(), result.size());
        for (var i = 0; i < books.size(); i++) {
            Assertions.assertEquals(books.get(i).getOriginalTitle(), result.get(i).originalTitle);
        }
    }

    @Test
    public void givenBookExistsWithStock_whenGettingAllBooks_thenReturnsBooks() {
        // Arrange
        var books = Instancio.ofList(Book.class).size(10).create();
        when(this.repository.findAllByAvailability()).thenReturn(books);
        // Act
        var result = service.getAllAvailableBooks();
        // Assert
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(books.size(), result.size());
        for (var i = 0; i < books.size(); i++) {
            Assertions.assertEquals(books.get(i).getOriginalTitle(), result.get(i).originalTitle);
        }
    }

    @Test
    public void givenValidBookTitle_whenGettingBookByTitle_thenReturnsBook() {
        // Arrange
        var bookTitle = "title";
        var books = Instancio.ofList(Book.class).size(10).create();
        when(this.repository.findByTitle(bookTitle)).thenReturn(books);
        // Act
        var result = service.getBookByTitle(bookTitle);
        // Assert
        Assertions.assertNotNull(result);
    }

    @Test
    public void givenInvalidBookTitle_whenGettingBookByTitle_thenThrowsException() {
        // Arrange
        var bookTitle = "title";
        var errorMessage = "Book not found with the Title: " + bookTitle;
        when(this.repository.findByTitle(bookTitle)).thenReturn(null);
        // Act
        Exception exception = assertThrows(EntityNotFoundException.class, () -> service.getBookByTitle(bookTitle));
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenValidBookOriginalTitle_whenGettingBookByOriginalTitle_thenReturnsBook() {
        // Arrange
        var bookOriginalTitle = "OriginalTitle";
        var books = Instancio.ofList(Book.class).size(10).create();
        when(this.repository.findByOriginalTitle(bookOriginalTitle)).thenReturn(books);
        // Act
        var result = service.getBookByOriginalTitle(bookOriginalTitle);
        // Assert
        Assertions.assertNotNull(result);
    }

    @Test
    public void givenInvalidBookOriginalTitle_whenGettingBookByTitle_thenThrowsException() {
        // Arrange
        var bookOriginalTitle = "OriginalTitle";
        var errorMessage = "Book not found with the Original Title: " + bookOriginalTitle;
        when(this.repository.findByOriginalTitle(bookOriginalTitle)).thenReturn(null);
        // Act
        Exception exception = assertThrows(EntityNotFoundException.class, () -> service.getBookByOriginalTitle(bookOriginalTitle));
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenValidBookIsbn_whenGettingBookByIsbn_thenReturnsBook() {
        // Arrange
        var bookIsbn = "Isbn";
        var book = Instancio.create(Book.class);
        when(this.repository.findByIsbn(bookIsbn)).thenReturn(book);
        // Act
        var result = service.getBookByISBN(bookIsbn);
        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(book.getOriginalTitle(), result.originalTitle);
    }

    @Test
    public void givenInvalidBookIsbn_whenGettingBookByIsbn_thenThrowsException() {
        // Arrange
        var bookIsbn = "Isbn";
        var errorMessage = "Book not found with the ISBN: " + bookIsbn;
        when(this.repository.findByIsbn(bookIsbn)).thenReturn(null);
        // Act
        Exception exception = assertThrows(EntityNotFoundException.class, () -> service.getBookByISBN(bookIsbn));
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenValidBookSynopsis_whenGettingBookBySynopsis_thenReturnsBook() {
        // Arrange
        var bookSynopsis = "Synopsis";
        var books = Instancio.ofList(Book.class).size(10).create();
        when(this.repository.findBySynopsis(bookSynopsis)).thenReturn(books);
        // Act
        var result = service.getBookBySynopsis(bookSynopsis);
        // Assert
        Assertions.assertNotNull(result);
    }

    @Test
    public void givenInvalidBookSynopsis_whenGettingBookBySynopsis_thenThrowsException() {
        // Arrange
        var bookSynopsis = "Synopsis";
        var errorMessage = "Book not found with the Synopsis: " + bookSynopsis;
        when(this.repository.findBySynopsis(bookSynopsis)).thenReturn(null);
        // Act
        Exception exception = assertThrows(EntityNotFoundException.class, () -> service.getBookBySynopsis(bookSynopsis));
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenValidBookId_whenGettingBookById_thenReturnsBook() {
        // Arrange
        var bookId = 1;
        var book = Instancio.create(Book.class);
        when(this.repository.findById(bookId)).thenReturn(Optional.ofNullable(book));
        // Act
        var result = service.getBookById(bookId);
        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(book.getOriginalTitle(), result.originalTitle);
    }

    @Test
    public void givenInvalidBookId_whenGettingBookById_thenThrowsException() {
        // Arrange
        var bookId = 1;
        var errorMessage = "Book not found with the Id: " + bookId;
        when(this.repository.findById(bookId)).thenReturn(Optional.ofNullable(null));
        // Act
        Exception exception = assertThrows(EntityNotFoundException.class, () -> service.getBookById(bookId));
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenValidBookId_whenDeletingBook_thenLogsSuccessMessage() {
        // Arrange
        var bookId = 1;
        var expectedMessage = "Book deleted with success.";
        doNothing().when(this.repository).deleteById(bookId);
        // Act
        service.deleteBook(bookId);
        // Assert
        assertThat(logCaptor.getInfoLogs()).containsExactly(expectedMessage);
    }

    @Test
    public void givenInvalidBook_whenUpdateBook_thenThrowsException() {
        // Arrange
        var bookId = 1;
        var errorMessage = "Book received is null";
        // Act
        Exception exception = assertThrows(EntityNullException.class, () -> service.updateBook(bookId, null));
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void givenBookIdThatDoesNot_whenUpdateBook_thenThrowsException() {
        // Arrange
        var bookId = 1;
        var bookDto = Instancio.create(BookUpdateRequestDto.class);
        var errorMessage = "Book not found with the Id: " + bookId;
        when(this.repository.findById(bookId)).thenReturn(Optional.ofNullable(null));
        // Act
        Exception exception = assertThrows(EntityNotFoundException.class, () -> service.updateBook(bookId, bookDto));
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
        verify(repository, times(1)).findById(bookId);
        verify(repository, times(0)).save(any(Book.class));
    }

    @Test
    public void givenValidBook_whenErrorOccursWhileUpdating_thenThrowsException() {
        // Arrange
        var bookId = 1;
        var bookDto = Instancio.create(BookUpdateRequestDto.class);
        var book = Instancio.create(Book.class);
        var errorMessage = "Error occurred while Updating Book";
        var expectedMessage = "Error occurred while upserting the book information";
        when(this.repository.findById(bookId)).thenReturn(Optional.ofNullable(book));
        when(this.repository.save(any(Book.class))).thenThrow(new SaveEntityException(errorMessage));
        // Act
        Exception exception = assertThrows(SaveEntityException.class, () -> service.updateBook(bookId, bookDto));
        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
        assertThat(logCaptor.getErrorLogs()).containsExactly(expectedMessage);
        verify(repository, times(1)).findById(bookId);
        verify(repository, times(1)).save(any(Book.class));
    }

    @Test
    public void givenValidBook_whenUpdating_thenLogsSuccessMessage() {
        // Arrange
        var expectedMessage = "Book saved with success.";
        var bookId = 1;
        var bookDto = Instancio.create(BookUpdateRequestDto.class);
        var book = Instancio.create(Book.class);
        when(this.repository.findById(bookId)).thenReturn(Optional.ofNullable(book));
        when(this.repository.save(any(Book.class))).thenReturn(book);
        // Act
        service.updateBook(bookId, bookDto);
        // Assert
        verify(repository, times(1)).findById(bookId);
        verify(repository, times(1)).save(any(Book.class));
        assertThat(logCaptor.getInfoLogs()).containsExactly(expectedMessage);
    }

    @Test
    public void  givenSearchRequest_whenGettingBooksThatExist_thenReturnBooks(){
        // Arrange
        var optionalString = Optional.ofNullable("string");
        var optionalFloat = Optional.ofNullable(1.0);
        var optionalBool = Optional.ofNullable(false);
        var optionalBookAvailabilityDto = Optional.ofNullable(BookAvailabilityDto.AVAILABLE);

        var errorMessage = "Book not found with the search parameters sent";
        when(this.repository.findAll(any(Specification.class))).thenReturn(new ArrayList<Book>());

        // Act
        Exception exception = assertThrows(EntityNotFoundException.class, () -> this.service.searchBooks(optionalString, optionalString, optionalString, optionalString,
                optionalBool, optionalFloat, optionalFloat, optionalBool, optionalBookAvailabilityDto));

        // Assert
        Assertions.assertEquals(errorMessage, exception.getMessage());
        assertThat(logCaptor.getWarnLogs()).containsExactly(errorMessage);

    }
}