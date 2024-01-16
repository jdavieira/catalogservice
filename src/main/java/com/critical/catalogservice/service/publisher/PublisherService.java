package com.critical.catalogservice.service.publisher;

import com.critical.catalogservice.data.entity.Publisher;
import com.critical.catalogservice.data.repository.PublisherRepository;
import com.critical.catalogservice.dtos.PublisherDto;
import com.critical.catalogservice.service.publisher.mapper.PublisherMapper;
import com.critical.catalogservice.util.exception.EntityNullException;
import com.critical.catalogservice.util.exception.SaveEntityException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherService {

    private static final Logger logger = LoggerFactory.getLogger(PublisherService.class);

    private final PublisherRepository repository;

    @Autowired
    public PublisherService(PublisherRepository repository) {

        this.repository = repository;
    }

    public List<PublisherDto> getAllPublishers() {

        var publishers = this.repository.findAll();
        return PublisherMapper.MAPPER.mapPublishersToPublishersDto(publishers);
    }

    public PublisherDto getPublisherById(int id) {

        var publisher = this.repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Publisher not found with the Id: " + id));
        return PublisherMapper.MAPPER.mapPublisherToPublisherDto(publisher);
    }

    public int createPublisher(PublisherDto publisher) {
        if (null == publisher) {
            logger.warn("Publisher Information received is null.");
            throw new EntityNullException("Publisher received is null");
        }

        var newPublisher = PublisherMapper.MAPPER.mapPublisherDtoToPublisher(publisher);
        return SavePublisher(newPublisher);
    }

    public void deletePublisher(int id) {

        this.repository.deleteById(id);
        logger.info("Publisher deleted with success.");
    }

    public boolean updatePublisher(int id, PublisherDto publisher) {
        if (null == publisher) {
            logger.warn("Publisher Information received is null.");
            throw new EntityNullException("Publisher received is null");
        }

        var existingPublisher = this.repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Publisher not found with the Id: " + id));
        existingPublisher.setName(publisher.name);
        SavePublisher(existingPublisher);
        return true;
    }

    private int SavePublisher(Publisher publisher) {

        try {
            var savedPublisher = this.repository.save(publisher);
            logger.info("Publisher saved with success");
            return savedPublisher.getId();
        } catch (Exception exception) {
            logger.error("Error occurred while saving the publisher information", exception);
            throw new SaveEntityException(exception.getMessage());
        }
    }
}