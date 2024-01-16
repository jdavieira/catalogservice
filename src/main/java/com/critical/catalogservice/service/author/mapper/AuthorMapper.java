package com.critical.catalogservice.service.author.mapper;

import com.critical.catalogservice.data.entity.Author;
import com.critical.catalogservice.dtos.AuthorDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AuthorMapper {
    AuthorMapper MAPPER = Mappers.getMapper(AuthorMapper.class);

    List<AuthorDto> mapAuthorsToAuthorsDto(List<Author> entity);

    AuthorDto mapAuthorToAuthorDto(Author entity);

    Author mapAuthorDtoToAuthor(AuthorDto dto);
    
}