package com.critical.catalogservice.service.rabbitMq;

import com.critical.catalogservice.data.event.UpdateBookStockEvent;
import com.critical.catalogservice.service.book.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpdateBookStockListener {

    @Autowired
    private BookService service;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "catalog.queue.update-book-stock", durable = "true"),
            exchange = @Exchange(value = "catalog.queue.update-book-stock-exchange", ignoreDeclarationExceptions = "true"),
            key = "catalog.queue.catalog.queue.update-book-stock-routing-key"))
    public void onUpdateBookStock(UpdateBookStockEvent event) {
        log.info("Update Book Stock Event Received: " + event.bookId + " - " + event.stock);

        service.updateBookStock(event.bookId, event.stock);

        log.info("Update Book Stock Event finished: " + event.bookId + " - " + event.stock);
    }
}