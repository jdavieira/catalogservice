package com.critical.catalogservice.service.language.mapper;

import com.critical.catalogservice.data.entity.Language;
import com.critical.catalogservice.dtos.LanguageDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface LanguageMapper {

    LanguageMapper MAPPER = Mappers.getMapper(LanguageMapper.class);

    List<LanguageDto> mapLanguagesToLanguagesDto(List<Language> entity);

    LanguageDto mapLanguageToLanguageDto(Language entity);

    Language mapLanguageDtoToLanguage(LanguageDto dto);
}