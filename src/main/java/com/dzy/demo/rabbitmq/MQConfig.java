package com.dzy.demo.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MQConfig {
    public static final String QUEUE="queue";
    public static final String MIAOSHA_QUEUE="miaosha.queue";

    public static final String TOPIC_QUEUE1="topic.queue1";
    public static final String TOPIC_QUEUE2="topic.queue2";
    public static final String TOPIC_EXCHANGE="topicExchange";
    public static final String FANOUT_EXCHANGE="fanoutExchange";
    public static final String HEADERS_EXCHANGE="headersExchange";
    public static final String HEADERS_QUEUE="headers.queue";


    @Bean
    public Queue getMiaoShaQueue(){
        return new Queue(MIAOSHA_QUEUE,true);
    }

    /**
     * Direct 模式 交换机Exchange
     */
    @Bean
    public Queue getQueue(){
        return new Queue(QUEUE,true);
    }

    /**
     * Topic 模式 交换机Exchange
     */
    @Bean
    public Queue getTopicQueue1() {
        return new Queue(TOPIC_QUEUE1, true);
    }

    @Bean
    public Queue getTopicQueue2() {
        return new Queue(TOPIC_QUEUE2, true);
    }

    @Bean
    public TopicExchange getTopicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding topicBinding1(){
        return BindingBuilder.bind(getTopicQueue1()).to(getTopicExchange()).with("topic.key1");
    }

    @Bean
    public Binding topicBinding2(){
        return BindingBuilder.bind(getTopicQueue2()).to(getTopicExchange()).with("topic.#");
    }

    /**
     * Fanout模式 交换机Exchange
     */
    @Bean
    public FanoutExchange getFanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding FanoutBinding1(){
        return BindingBuilder.bind(getTopicQueue1()).to(getFanoutExchange());
    }

    @Bean
    public Binding FanoutBinding2(){
        return BindingBuilder.bind(getTopicQueue2()).to(getFanoutExchange());
    }

    /**
     * Headers模式 交换机Exchange
     */
    @Bean
    public HeadersExchange getHeadersExchange() {
        return new HeadersExchange(HEADERS_EXCHANGE);
    }

    @Bean
    public Queue getHeadersQueue() {
        return new Queue(HEADERS_QUEUE, true);
    }

    @Bean
    public Binding HeadersBinding(){
        Map<String,Object> map=new HashMap<>();
        map.put("key1","value1");
        map.put("key2","value2");
        return BindingBuilder.bind(getHeadersQueue()).to(getHeadersExchange()).whereAll(map).match();
    }
}
