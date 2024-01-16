package com.critical.catalogservice.service.author;

import com.critical.catalogservice.data.entity.Author;
import com.critical.catalogservice.data.repository.AuthorRepository;
import com.critical.catalogservice.dtos.AuthorDto;
import com.critical.catalogservice.service.author.mapper.AuthorMapper;
import com.critical.catalogservice.util.exception.EntityNullException;
import com.critical.catalogservice.util.exception.SaveEntityException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);

    private final AuthorRepository repository;

    @Autowired
    public AuthorService(AuthorRepository repository) {

        this.repository = repository;
    }

    public List<AuthorDto> getAllAuthors() {

        var authors = this.repository.findAll();
        return AuthorMapper.MAPPER.mapAuthorsToAuthorsDto(authors);
    }

    public AuthorDto getAuthorById(int id) {

        var author = this.repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Author not found with the Id: " + id));
        return AuthorMapper.MAPPER.mapAuthorToAuthorDto(author);
    }

    public int createAuthor(AuthorDto author) {
        if (null == author) {
            logger.warn("Author Information received is null.");
            throw new EntityNullException("Author received is null");
        }
        var newAuthor = AuthorMapper.MAPPER.mapAuthorDtoToAuthor(author);
        return saveAuthor(newAuthor);
    }

    public void deleteAuthor(int id) {

        this.repository.deleteById(id);
        logger.info("Author deleted with success.");
    }

    public boolean updateAuthor(int id, AuthorDto author) {
        if (null == author) {
            logger.warn("Author Information received is null.");
            throw new EntityNullException("Author received is null");
        }
        var existingAuthor = this.repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Author not found with the Id: " + id));
        existingAuthor.setName(author.name);
        saveAuthor(existingAuthor);
        return true;
    }

    private int saveAuthor(Author author) {

        try {
            var savedAuthor = this.repository.save(author);
            logger.info("Author saved with success");
            return savedAuthor.getId();
        } catch (Exception exception) {
            logger.error("Error occurred while saving the author information", exception);
            throw new SaveEntityException(exception.getMessage());
        }
    }
}