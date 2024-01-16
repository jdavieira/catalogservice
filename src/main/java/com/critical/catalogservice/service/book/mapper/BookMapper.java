package com.critical.catalogservice.service.book.mapper;

import com.critical.catalogservice.data.entity.Book;
import com.critical.catalogservice.dtos.book.BookDto;
import com.critical.catalogservice.dtos.book.BookRequestDto;
import com.critical.catalogservice.service.author.mapper.AuthorMapper;
import com.critical.catalogservice.service.format.mapper.FormatMapper;
import com.critical.catalogservice.service.genre.mapper.GenreMapper;
import com.critical.catalogservice.service.language.mapper.LanguageMapper;
import com.critical.catalogservice.service.publisher.mapper.PublisherMapper;
import com.critical.catalogservice.service.tag.mapper.TagMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {AuthorMapper.class, LanguageMapper.class, FormatMapper.class, TagMapper.class, PublisherMapper.class, GenreMapper.class})
public interface BookMapper {

    BookMapper MAPPER = Mappers.getMapper(BookMapper.class);

    List<BookDto> mapBooksToBooksDto(List<Book> books);

    BookDto mapBookToBookDto(Book book);

    Book mapBookRequestDtoToBook(BookRequestDto bookRequest);
}