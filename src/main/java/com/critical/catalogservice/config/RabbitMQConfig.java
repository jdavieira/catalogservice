package com.critical.catalogservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitMQConfig {

    private final CachingConnectionFactory cachingConnectionFactory;

    @Value("${catalog.rabbitmq.queue}")
    String queueName;

    @Value("${catalog.rabbitmq.queue-update-book-stock}")
    String queueUpdateBookStockName;

    public RabbitMQConfig(CachingConnectionFactory cachingConnectionFactory) {

        this.cachingConnectionFactory = cachingConnectionFactory;
    }

    @Bean
    public Queue queue() {

        return new Queue(queueName, false);
    }

    @Bean
    public Queue CreateCatalogUpdateBookStock() {

        return new Queue(queueUpdateBookStockName);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {

        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(Jackson2JsonMessageConverter converter){
        RabbitTemplate template = new RabbitTemplate(cachingConnectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}