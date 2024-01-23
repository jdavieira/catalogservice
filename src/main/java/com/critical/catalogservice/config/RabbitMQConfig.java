package com.critical.catalogservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
    @Value("${catalog.rabbitmq.queue-update-book-stock}")
    String queueUpdateBookStockName;

    @Value("${catalog.rabbitmq.queue-book-stock-request}")
    String queueBookStockRequestName;

    @Value("${catalog.rabbitmq.queue.exchange}")
    private  String exchange;

    @Value("${catalog.queue.update-book-stock-exchange}")
    private  String exchangeUpdateBookStock;

    @Value("${catalog.rabbitmq.queue.routing.key}")
    private  String routingKey;

    @Value("${catalog.queue.catalog.queue.update-book-stock-routing-key}")
    private String stockRoutingKey;

    public RabbitMQConfig(CachingConnectionFactory cachingConnectionFactory) {

        this.cachingConnectionFactory = cachingConnectionFactory;
    }

    @Bean
    public Queue CreateCatalogUpdateBookStock() {

        return new Queue(queueUpdateBookStockName);
    }

    @Bean
    public Queue CreateBookStockRequestQueue() {

        return new Queue(queueBookStockRequestName);
    }


    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding jsonBinding(){
        return BindingBuilder.bind(CreateBookStockRequestQueue())
                .to(exchange())
                .with(routingKey);
    }

    @Bean
    public Binding jsonConsumerBinding(){
        return BindingBuilder.bind(CreateCatalogUpdateBookStock())
                .to(new TopicExchange(exchangeUpdateBookStock))
                .with(stockRoutingKey);
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