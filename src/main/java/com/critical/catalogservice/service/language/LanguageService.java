package com.critical.catalogservice.service.language;

import com.critical.catalogservice.data.entity.Language;
import com.critical.catalogservice.data.repository.LanguageRepository;
import com.critical.catalogservice.dtos.LanguageDto;
import com.critical.catalogservice.service.language.mapper.LanguageMapper;
import com.critical.catalogservice.util.exception.EntityNullException;
import com.critical.catalogservice.util.exception.SaveEntityException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {
    private static final Logger logger = LoggerFactory.getLogger(LanguageService.class);
    private final LanguageRepository repository;

    @Autowired
    public LanguageService(LanguageRepository repository) {

        this.repository = repository;
    }

    public List<LanguageDto> getAllLanguages() {
        var languages= this.repository.findAll();
        return LanguageMapper.MAPPER.mapLanguagesToLanguagesDto(languages);
    }

    public LanguageDto getLanguageById(int id)
    {
        var language = this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Language not found with the Id: " + id));
        return LanguageMapper.MAPPER.mapLanguageToLanguageDto(language);
    }

    public void deleteLanguage(int id){
        this.repository.deleteById(id);
        logger.info("Language deleted with success.");
    }

    public boolean updateLanguage(int id, LanguageDto language){
        if (null == language) {
            logger.warn("Language Information received is null.");
            throw new EntityNullException("Language received is null");
        }

        var existingLanguage  = this.repository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Language not found with the Id: " + id));

        existingLanguage.setCulture(language.culture);
        existingLanguage.setName(language.name);

        saveLanguage(existingLanguage);

        return true;
    }

    public int createLanguage(LanguageDto language)
    {
        if (null == language) {
            logger.warn("Language Information received is null.");
            throw new EntityNullException("Language received is null");
        }

        var newLanguage = LanguageMapper.MAPPER.mapLanguageDtoToLanguage(language);

        return saveLanguage(newLanguage);
    }

    private int saveLanguage(Language language) {

        try {
            var savedLanguage=  this.repository.save(language);
            logger.info("Language saved with success.");
            return savedLanguage.getId();
        }catch (Exception exception) {
            logger.error("Error occurred while upserting the language information", exception);
            throw new SaveEntityException(exception.getMessage());
        }
    }
}