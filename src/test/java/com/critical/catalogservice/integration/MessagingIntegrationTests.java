package com.critical.catalogservice.integration;

import com.critical.catalogservice.data.event.UpdateBookStockEvent;
import com.critical.catalogservice.dtos.*;
import com.critical.catalogservice.dtos.book.BookDto;
import com.critical.catalogservice.dtos.book.BookRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
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
public class MessagingIntegrationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void givenBookStockUpdate_whenBookExists_thenStockUpdatedWithSuccess() throws Exception {
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

        this.mockMvc.perform(post("/v1/api/book").content(requestBody)
                        .contentType("application/json"))
                        .andExpect(status().isOk());

        var expectedStock = book.stockAvailable + 10000;

        var event = new UpdateBookStockEvent(1, 10000);

        // Act
        rabbitTemplate.convertAndSend("catalog-service-exchange", "catalog-service-routing-key", event);

        // Assert
        await().atMost(10000, TimeUnit.SECONDS).until(() -> true);

        var mvcResult = this.mockMvc.perform(get("/v1/api/book/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertEquals(book.title,response.title);
        assertEquals(book.originalTitle,response.originalTitle);
        assertEquals(expectedStock, response.stockAvailable);
    }


}