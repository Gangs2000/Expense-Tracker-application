package com.expensetracker.gui.OTP.RabbitMq;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String QUEUE1="expense-tracker-app-OTP", QUEUE2="expense-tracker-app-daily-email-trigger", QUEUE3="expense-tracker-app-monthly-email-trigger", QUEUE4="expense-tracker-app-yearly-email-trigger";
    public static final String EXCHANGE="expense-tracker-app-email-service";
    public static final String ROUTING_KEY1="otp", ROUTING_KEY2="daily", ROUTING_KEY3="monthly", ROUTING_KEY4="yearly";

    @Bean
    public Queue queue1(){
        return new Queue(QUEUE1);
    }

    @Bean
    public Queue queue2(){
        return new Queue(QUEUE2);
    }

    @Bean
    public Queue queue3(){
        return new Queue(QUEUE3);
    }

    @Bean
    public Queue queue4(){
        return new Queue(QUEUE4);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding1(TopicExchange topicExchange){
        return BindingBuilder.bind(queue1()).to(topicExchange).with(ROUTING_KEY1);
    }

    @Bean
    public Binding binding2(TopicExchange topicExchange){
        return BindingBuilder.bind(queue2()).to(topicExchange).with(ROUTING_KEY2);
    }

    @Bean
    public Binding binding3(TopicExchange topicExchange){
        return BindingBuilder.bind(queue3()).to(topicExchange).with(ROUTING_KEY3);
    }

    @Bean
    public Binding binding4(TopicExchange topicExchange){
        return BindingBuilder.bind(queue4()).to(topicExchange).with(ROUTING_KEY4);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate=new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
