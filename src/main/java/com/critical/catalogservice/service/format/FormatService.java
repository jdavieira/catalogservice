package com.critical.catalogservice.service.format;

import com.critical.catalogservice.data.entity.Format;
import com.critical.catalogservice.data.repository.FormatRepository;
import com.critical.catalogservice.dtos.FormatDto;

import com.critical.catalogservice.service.format.mapper.FormatMapper;
import com.critical.catalogservice.util.exception.EntityNullException;
import com.critical.catalogservice.util.exception.SaveEntityException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormatService {
    private static final Logger logger = LoggerFactory.getLogger(FormatService.class);

    private final FormatRepository repository;

    @Autowired
    public FormatService(FormatRepository repository) {

        this.repository = repository;
    }

    public List<FormatDto> getAllFormats() {

        var formats = this.repository.findAll();
        return FormatMapper.MAPPER.mapFormatsToFormatsDto(formats);
    }

    public FormatDto getFormatById(int id) {

        var format = this.repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Format not found with the Id: " + id));
        return FormatMapper.MAPPER.mapFormatToFormatDto(format);
    }

    public int createFormat(FormatDto format) {
        if (null == format) {
            logger.warn("Format Information received is null.");
            throw new EntityNullException("Format received is null");
        }
        var newFormat = FormatMapper.MAPPER.mapFormatDtoToFormat(format);
        return saveFormat(newFormat);
    }

    public void deleteFormat(int id) {

        this.repository.deleteById(id);
        logger.info("Format deleted with success.");
    }

    public boolean updateFormat(int id, FormatDto format) {
        if (null == format) {
            logger.warn("Format Information received is null.");
            throw new EntityNullException("Format received is null");
        }
        var existingFormat = this.repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Format not found with the Id: " + id));
        existingFormat.setName(format.name);
        saveFormat(existingFormat);
        return true;
    }

    private int saveFormat(Format format) {

        try {
            var savedFormat = this.repository.save(format);
            logger.info("Format saved with success");
            return savedFormat.getId();
        } catch (Exception exception) {
            logger.error("Error occurred while saving the Format information", exception);
            throw new SaveEntityException(exception.getMessage());
        }
    }
}