package com.critical.catalogservice.service.genre;

import com.critical.catalogservice.data.entity.Genre;
import com.critical.catalogservice.data.repository.GenreRepository;
import com.critical.catalogservice.dtos.GenreDto;
import com.critical.catalogservice.service.genre.mapper.GenreMapper;
import com.critical.catalogservice.util.exception.EntityNullException;
import com.critical.catalogservice.util.exception.SaveEntityException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    private static final Logger logger = LoggerFactory.getLogger(GenreService.class);

    private final GenreRepository repository;

    @Autowired
    public GenreService(GenreRepository repository) {

        this.repository = repository;
    }

    public List<GenreDto> getAllGenres() {

        var genres = this.repository.findAll();
        return GenreMapper.MAPPER.mapGenresToGenresDto(genres);
    }

    public GenreDto getGenreById(int id) {

        var genre = this.repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Genre not found with the Id: " + id));
        return GenreMapper.MAPPER.mapGenreToGenreDto(genre);
    }

    public int createGenre(GenreDto genre) {
        if (null == genre) {
            logger.warn("Genre Information received is null.");
            throw new EntityNullException("Genre received is null");
        }

        var newGenre = GenreMapper.MAPPER.mapGenreDtoToGenre(genre);
        return saveGenre(newGenre);
    }

    public void deleteGenre(int id) {

        this.repository.deleteById(id);
        logger.info("Genre deleted with success.");
    }

    public boolean updateGenre(int id, GenreDto genre) {
        if (null == genre) {
            logger.warn("Genre Information received is null.");
            throw new EntityNullException("Genre received is null");
        }
        var existingGenre = this.repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Genre not found with the Id: " + id));
        existingGenre.setName(genre.name);
        saveGenre(existingGenre);
        return true;
    }

    private int saveGenre(Genre genre) {

        try {
            var savedGenre = this.repository.save(genre);
            logger.info("Genre saved with success");
            return savedGenre.getId();
        } catch (Exception exception) {
            logger.error("Error occurred while saving the Genre information", exception);
            throw new SaveEntityException(exception.getMessage());
        }
    }
}