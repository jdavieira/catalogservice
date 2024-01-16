package com.critical.catalogservice.service.format.mapper;

import com.critical.catalogservice.data.entity.Format;
import com.critical.catalogservice.dtos.FormatDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FormatMapper {
    FormatMapper MAPPER = Mappers.getMapper(FormatMapper.class);

    List<FormatDto> mapFormatsToFormatsDto(List<Format> entity);

    FormatDto mapFormatToFormatDto(Format entity);

    Format mapFormatDtoToFormat(FormatDto dto);
}