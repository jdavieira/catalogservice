package com.critical.catalogservice.service.tag.mapper;

import com.critical.catalogservice.data.entity.Tag;
import com.critical.catalogservice.dtos.TagDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TagMapper {

    TagMapper MAPPER = Mappers.getMapper(TagMapper.class);

    List<TagDto> mapTagsToTagsDto(List<Tag> entity);

    TagDto mapTagToTagDto(Tag entity);

    Tag mapTagDtoToTag(TagDto dto);
}