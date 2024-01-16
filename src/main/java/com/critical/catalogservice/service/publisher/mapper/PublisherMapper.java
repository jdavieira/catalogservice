package com.critical.catalogservice.service.publisher.mapper;

import com.critical.catalogservice.data.entity.Publisher;
import com.critical.catalogservice.dtos.PublisherDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PublisherMapper {

    PublisherMapper MAPPER = Mappers.getMapper(PublisherMapper.class);

    List<PublisherDto> mapPublishersToPublishersDto(List<Publisher> entity);

    PublisherDto mapPublisherToPublisherDto(Publisher entity);

    Publisher mapPublisherDtoToPublisher(PublisherDto dto);
}