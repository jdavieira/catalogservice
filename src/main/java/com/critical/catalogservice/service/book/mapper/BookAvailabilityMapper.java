package com.critical.catalogservice.service.book.mapper;

import com.critical.catalogservice.data.entity.enums.BookAvailability;
import com.critical.catalogservice.dtos.book.BookAvailabilityDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookAvailabilityMapper {

    BookAvailabilityMapper MAPPER = Mappers.getMapper(BookAvailabilityMapper.class);
    BookAvailabilityDto map(BookAvailability source);

    BookAvailability map(BookAvailabilityDto source);
}