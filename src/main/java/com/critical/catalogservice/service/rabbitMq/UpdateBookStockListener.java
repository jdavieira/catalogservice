package com.critical.catalogservice.service.rabbitMq;

import com.critical.catalogservice.data.event.UpdateBookStockEvent;
import com.critical.catalogservice.service.book.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UpdateBookStockListener {

    @Autowired
    private BookService service;

    @RabbitListener(queues = {"catalog.queue.update-book-stock"})
    public void onUpdateBookStock(UpdateBookStockEvent event) {
        log.info("Update Book Stock Event Received: ", event);

        if(null == event){
            log.error("Update Book Stock Event Received is null");
            return;
        }

        service.updateBookStock(event.id, event.stock);
    }
}