package com.critical.catalogservice.service.genre.mapper;

import com.critical.catalogservice.data.entity.Genre;
import com.critical.catalogservice.dtos.GenreDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface GenreMapper {

    GenreMapper MAPPER = Mappers.getMapper(GenreMapper.class);

    List<GenreDto> mapGenresToGenresDto(List<Genre> entity);

    GenreDto mapGenreToGenreDto(Genre entity);

    Genre mapGenreDtoToGenre(GenreDto dto);
}