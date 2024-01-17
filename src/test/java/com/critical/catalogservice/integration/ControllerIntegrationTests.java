package com.critical.catalogservice.integration;

import com.critical.catalogservice.dtos.*;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
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
        var mvcResult = this.mockMvc.perform(get("/v1/api/author").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains(author.name));
        assertTrue(mvcResult.getResponse().getContentAsString().contains(author.originalName));
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
        var mvcResult = this.mockMvc.perform(get("/v1/api/books").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains(book.title));
        assertTrue(mvcResult.getResponse().getContentAsString().contains(book.originalTitle));
    }
}