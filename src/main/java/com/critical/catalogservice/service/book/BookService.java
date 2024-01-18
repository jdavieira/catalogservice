package com.critical.catalogservice.service.book;

import com.critical.catalogservice.data.entity.Book;
import com.critical.catalogservice.data.repository.BookRepository;
import com.critical.catalogservice.data.specification.BookSpecifications;
import com.critical.catalogservice.dtos.book.BookAvailabilityDto;
import com.critical.catalogservice.dtos.book.BookDto;
import com.critical.catalogservice.dtos.book.BookRequestDto;
import com.critical.catalogservice.dtos.book.BookUpdateRequestDto;
import com.critical.catalogservice.service.book.mapper.BookAvailabilityMapper;
import com.critical.catalogservice.service.book.mapper.BookMapper;
import com.critical.catalogservice.util.exception.EntityNullException;
import com.critical.catalogservice.util.exception.SaveEntityException;
import jakarta.persistence.EntityNotFoundException;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.scheduling.JobScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private final BookRepository repository;

    private final JobScheduler jobScheduler;

    public BookService(BookRepository repository, JobScheduler jobScheduler) {

        this.repository = repository;
        this.jobScheduler = jobScheduler;
    }

    public List<BookDto> getAllBooks() {

        var books = this.repository.findAll();
        return BookMapper.MAPPER.mapBooksToBooksDto(books);
    }

    public List<BookDto> getAllAvailableBooks() {

        var books = this.repository.findAllByAvailability();
        return BookMapper.MAPPER.mapBooksToBooksDto(books);
    }

    public BookDto getBookById(int id) {

        var book = this.repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found with the Id: " + id));

        return BookMapper.MAPPER.mapBookToBookDto(book);
    }

    public List<BookDto> getBookBySynopsis(String synopsis) {

        var books = this.repository.findBySynopsis(synopsis);
        if (null == books || books.isEmpty()) {
            var message = "Book not found with the Synopsis: " + synopsis;
            logger.warn(message);
            throw new EntityNotFoundException(message);
        }
        return BookMapper.MAPPER.mapBooksToBooksDto(books);
    }

    public List<BookDto> getBookByTitle(String Title) {

        var books = this.repository.findByTitle(Title);
        if (null == books || books.isEmpty()) {
            var message = "Book not found with the Title: " + Title;
            logger.warn(message);
            throw new EntityNotFoundException(message);
        }
        return BookMapper.MAPPER.mapBooksToBooksDto(books);
    }

    public List<BookDto> getBookByOriginalTitle(String originalTitle) {

        var books = this.repository.findByOriginalTitle(originalTitle);
        if (null == books || books.isEmpty()) {
            var message = "Book not found with the Original Title: " + originalTitle;
            logger.warn(message);
            throw new EntityNotFoundException(message);
        }
        return BookMapper.MAPPER.mapBooksToBooksDto(books);
    }

    public BookDto getBookByISBN(String isbn) {

        var book = this.repository.findByIsbn(isbn);
        if (null == book) {
            var message = "Book not found with the ISBN: " + isbn;
            logger.warn(message);
            throw new EntityNotFoundException(message);
        }
        return BookMapper.MAPPER.mapBookToBookDto(book);
    }

    public void deleteBook(int id) {

        this.repository.deleteById(id);
        logger.info("Book deleted with success.");
    }

    @Transactional
    public int createBook(BookRequestDto bookRequest) {

        if (null == bookRequest) {
            logger.warn("Book Information received is null.");
            throw new EntityNullException("Book received is null");
        }
        var book = BookMapper.MAPPER.mapBookRequestDtoToBook(bookRequest);
        return saveBook(book);
    }

    private int saveBook(Book book) {

        try {
            var savedBook = this.repository.save(book);
            logger.info("Book saved with success.");
            return savedBook.getId();
        } catch (Exception exception) {
            logger.error("Error occurred while upserting the book information", exception);
            throw new SaveEntityException(exception.getMessage());
        }
    }

    @Job(name="Update Book Stock", retries=5)
    public void updateBookStock(int id, int stock) {

        try {
            var book = this.repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found with the Id: " + id));

            book.setStockAvailable(book.getStockAvailable() + stock);

            this.repository.save(book);
            logger.info("Book stock updated with success.");
        } catch (EntityNotFoundException ex) {
            logger.warn(ex.getMessage());
            jobScheduler.schedule(Instant.now().plusSeconds(20), () -> this.updateBookStock(id, stock));
        }
    }

    public boolean updateBook(int id, BookUpdateRequestDto book) {

        if (null == book) {
            logger.warn("Book Information received is null.");
            throw new EntityNullException("Book received is null");
        }
        var existingBook = this.repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found with the Id: " + id));
        existingBook.setIsbn(book.isbn);
        existingBook.setAvailability(BookAvailabilityMapper.MAPPER.map(book.availability));
        existingBook.setEdition(book.edition);
        existingBook.setEditionDate(book.editionDate);
        existingBook.setReleaseDate(book.releaseDate);
        existingBook.setSeries(book.isSeries);
        existingBook.setStockAvailable(book.stockAvailable);
        existingBook.setOriginalTitle(book.originalTitle);
        existingBook.setTitle(book.title);
        existingBook.setSynopsis(book.synopsis);
        existingBook.setPrice(book.price);
        existingBook.setPromotionalPrice(book.promotionalPrice);
        saveBook(existingBook);
        return true;
    }

    public List<BookDto> searchBooks(
            Optional<String> author, Optional<String> tag, Optional<String> genre, Optional<String> language, Optional<Boolean> IsSeries, Optional<Double> minPrice, Optional<Double> maxPrice, Optional<Boolean> promotionStatus, Optional<BookAvailabilityDto> availability) {

        Specification<Book> spec = Specification.where(null);
        if (author.isPresent()) {
            spec = spec.and(BookSpecifications.hasAuthorEqualTo(author.get()));
        }
        if (tag.isPresent()) {
            spec = spec.and(BookSpecifications.hasTagEqualTo(tag.get()));
        }
        if (genre.isPresent()) {
            spec = spec.and(BookSpecifications.hasGenreEqualTo(genre.get()));
        }
        if (language.isPresent()) {
            spec = spec.and(BookSpecifications.hasLanguageEqualTo(language.get()));
        }
        if (IsSeries.isPresent()) {
            spec = spec.and(BookSpecifications.hasBooleanProperty(IsSeries.get(), "is_series"));
        }
        if (minPrice.isPresent()) {
            spec = spec.and(BookSpecifications.hasFloatPropertyGreaterThan(minPrice.get(), "price"));
        }
        if (maxPrice.isPresent()) {
            spec = spec.and(BookSpecifications.hasFloatPropertyLessThan(maxPrice.get(), "price"));
        }
        if (availability.isPresent()) {
            spec = spec.and(BookSpecifications.hasIntProperty(availability.get().getValue(), "availability"));
        }
        if (promotionStatus.isPresent()) {
            spec = promotionStatus.get() ? spec.and(BookSpecifications.hasFloatPropertyGreaterThan(1.0, "promotional_price")) : spec.and(BookSpecifications.hasFloatPropertyEqualTo(0.0, "promotional_price"));
        }
        var books = repository.findAll(spec);
        if (null == books || books.isEmpty()) {
            var message = "Book not found with the search parameters sent";
            logger.warn(message);
            throw new EntityNotFoundException(message);
        }
        return BookMapper.MAPPER.mapBooksToBooksDto(books);
    }
}