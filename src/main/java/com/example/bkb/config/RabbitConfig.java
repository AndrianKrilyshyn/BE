package com.example.bkb.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "proctoring.exchange";
    public static final String TASKS_QUEUE = "proctoring.tasks";
    public static final String RESULTS_QUEUE = "proctoring.results";

    @Bean
    TopicExchange proctoringExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    Queue tasksQueue() {
        return QueueBuilder.durable(TASKS_QUEUE).build();
    }

    @Bean
    Queue resultsQueue() {
        return QueueBuilder.durable(RESULTS_QUEUE).build();
    }

    @Bean
    Binding tasksBinding(Queue tasksQueue, TopicExchange proctoringExchange) {
        return BindingBuilder.bind(tasksQueue).to(proctoringExchange).with(TASKS_QUEUE);
    }

    @Bean
    Binding resultsBinding(Queue resultsQueue, TopicExchange proctoringExchange) {
        return BindingBuilder.bind(resultsQueue).to(proctoringExchange).with(RESULTS_QUEUE);
    }

    @Bean
    MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
