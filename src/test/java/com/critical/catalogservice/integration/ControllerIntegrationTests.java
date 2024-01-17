package com.critical.catalogservice.integration;

import com.critical.catalogservice.dtos.*;
import com.critical.catalogservice.dtos.book.BookDto;
import com.critical.catalogservice.dtos.book.BookRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.instancio.Select.all;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc()
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ControllerIntegrationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void givenPublisherPostRequest_whenExecutedWithSuccess_thenGetShouldReturnPublisher() throws Exception {
        // Arrange
        var publisher = Instancio.of(PublisherDto.class).create();

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(publisher);

        // Act
        this.mockMvc.perform(post("/v1/api/publisher")
                        .content(requestBody).contentType("application/json"))
                .andExpect(status().isOk());

        // Assert
        var mvcResult = this.mockMvc.perform(get("/v1/api/publisher/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PublisherDto.class);

        assertEquals(publisher.name,response.name);
    }

    @Test
    public void givenGenrePostRequest_whenExecutedWithSuccess_thenGetShouldReturnGenres() throws Exception {
        // Arrange
        var genre = Instancio.of(GenreDto.class).create();

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(genre);

        // Act
        this.mockMvc.perform(post("/v1/api/genre")
                        .content(requestBody).contentType("application/json"))
                .andExpect(status().isOk());

        // Assert
        var mvcResult = this.mockMvc.perform(get("/v1/api/genre/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GenreDto.class);

        assertEquals(genre.name,response.name);
    }

    @Test
    public void givenFormatPostRequest_whenExecutedWithSuccess_thenGetShouldReturnFormats() throws Exception {
        // Arrange
        var format = Instancio.of(FormatDto.class).create();

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(format);

        // Act
        this.mockMvc.perform(post("/v1/api/format")
                        .content(requestBody).contentType("application/json"))
                .andExpect(status().isOk());

        // Assert
        var mvcResult = this.mockMvc.perform(get("/v1/api/format/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), FormatDto.class);

        assertEquals(format.name,response.name);
    }

    @Test
    public void givenLanguagePostRequest_whenExecutedWithSuccess_thenGetShouldReturnLanguages() throws Exception {
        // Arrange
        var language = Instancio.of(LanguageDto.class).create();

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(language);

        // Act
        this.mockMvc.perform(post("/v1/api/language")
                        .content(requestBody).contentType("application/json"))
                .andExpect(status().isOk());

        // Assert
        var mvcResult = this.mockMvc.perform(get("/v1/api/language/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), LanguageDto.class);

        assertEquals(language.name,response.name);
    }

    @Test
    public void givenTagPostRequest_whenExecutedWithSuccess_thenGetShouldReturnTags() throws Exception {
        // Arrange
        var tag = Instancio.of(TagDto.class).create();

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(tag);

        // Act
        this.mockMvc.perform(post("/v1/api/tag")
                        .content(requestBody).contentType("application/json"))
                .andExpect(status().isOk());

        // Assert
        var mvcResult = this.mockMvc.perform(get("/v1/api/tag/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TagDto.class);

        assertEquals(tag.name,response.name);
    }

    @Test
    public void givenAuthorPostRequest_whenExecutedWithSuccess_thenGetShouldReturnAuthors() throws Exception {
        // Arrange
        var author = Instancio.of(AuthorDto.class).create();

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(author);

        // Act
        this.mockMvc.perform(post("/v1/api/author")
                        .content(requestBody).contentType("application/json"))
                .andExpect(status().isOk());

        // Assert
        var mvcResult = this.mockMvc.perform(get("/v1/api/author/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AuthorDto.class);

        assertEquals(author.name,response.name);
        assertEquals(author.originalName,response.originalName);
    }

    @Test
    public void givenBookPostRequest_whenExecutedWithSuccess_thenGetShouldReturnBooks() throws Exception {
        // Arrange
        var book = Instancio.of(BookRequestDto.class)
                .ignore(all(field(AuthorDto.class, "id")))
                .ignore(all(field(LanguageDto.class, "id")))
                .ignore(all(field(GenreDto.class, "id")))
                .ignore(all(field(PublisherDto.class, "id")))
                .ignore(all(field(FormatDto.class, "id")))
                .ignore(all(field(TagDto.class, "id")))
                .create();

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(book);

        // Act
        this.mockMvc.perform(post("/v1/api/book")
                .content(requestBody).contentType("application/json"))
                .andExpect(status().isOk());

        // Assert
        var mvcResult = this.mockMvc.perform(get("/v1/api/book/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertEquals(book.title,response.title);
        assertEquals(book.originalTitle,response.originalTitle);
    }
}