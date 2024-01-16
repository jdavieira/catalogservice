package com.critical.catalogservice.service.tag;

import com.critical.catalogservice.data.entity.Tag;
import com.critical.catalogservice.data.repository.TagRepository;
import com.critical.catalogservice.dtos.TagDto;
import com.critical.catalogservice.service.tag.mapper.TagMapper;
import com.critical.catalogservice.util.exception.EntityNullException;
import com.critical.catalogservice.util.exception.SaveEntityException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private static final Logger logger = LoggerFactory.getLogger(TagService.class);

    private final TagRepository repository;

    @Autowired
    public TagService(TagRepository repository) {

        this.repository = repository;
    }

    public List<TagDto> getAllTags() {

        var tags = this.repository.findAll();
        return TagMapper.MAPPER.mapTagsToTagsDto(tags);
    }

    public TagDto getTagById(int id) {

        var tag = this.repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag not found with the Id: " + id));
        return TagMapper.MAPPER.mapTagToTagDto(tag);
    }

    public int createTag(TagDto tag) {
        if (null == tag) {
            logger.warn("Tag Information received is null.");
            throw new EntityNullException("Tag received is null");
        }

        var newTag = TagMapper.MAPPER.mapTagDtoToTag(tag);
        return SaveTag(newTag);
    }

    public void deleteTag(int id) {
        this.repository.deleteById(id);
        logger.info("Tag deleted with success.");
    }

    public boolean updateTag(int id, TagDto tag) {
        if (null == tag) {
            logger.warn("Tag Information received is null.");
            throw new EntityNullException("Tag received is null");
        }

        var existingTag = this.repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag not found with the Id: " + id));
        existingTag.setName(tag.name);
        SaveTag(existingTag);
        return true;
    }

    private int SaveTag(Tag tag) {

        try {
            var savedTag = this.repository.save(tag);
            logger.info("Tag saved with success");
            return savedTag.getId();
        } catch (Exception exception) {
            logger.error("Error occurred while saving the tag information", exception);
            throw new SaveEntityException(exception.getMessage());
        }
    }
}