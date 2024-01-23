package com.critical.catalogservice.service.rabbitMq;

import com.critical.catalogservice.data.event.UpdateBookStockEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookStockProducer {

    @Value("${catalog.rabbitmq.queue.routing.key}")
    private String routingKey;

    @Value("${catalog.rabbitmq.queue.exchange}")
    private String exchange;

    private final RabbitTemplate rabbitTemplate;

    public BookStockProducer(RabbitTemplate rabbitTemplate) {

        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendBockStockRequestMessage(int bookId, int stock) {

        log.info("Book stock request event sent: " + bookId + " - " + stock);
        rabbitTemplate.convertAndSend(exchange, routingKey, new UpdateBookStockEvent(bookId, stock));
    }
}