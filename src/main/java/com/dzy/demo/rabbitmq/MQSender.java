package com.dzy.demo.rabbitmq;


import com.dzy.demo.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {

    private Logger log= LoggerFactory.getLogger(MQSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendMiaoShaRequest(Object message){
        String msg=RedisService.beanToString(message);
        log.info("MQSender send:"+msg);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE,msg);
    }

    /*
    public void send(Object message){
        String msg=RedisService.beanToString(message);
        log.info("MQSender send:"+msg);
        amqpTemplate.convertAndSend(MQConfig.QUEUE,msg);
    }

    public void sendTopic(Object message){
        String msg=RedisService.beanToString(message);
        log.info("TopicQueue Sender Send:"+msg);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key1",msg+" 1");
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.ext",msg+" 2");
    }

    public void sendFanout(Object message){
        String msg=RedisService.beanToString(message);
        log.info("FanoutQueue Sender Send:"+msg);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",msg);
    }

    public void sendHeaders(Object message){
        String msg=RedisService.beanToString(message);
        log.info("HeadersQueue Sender Send:"+msg);
        MessageProperties messageProperties=new MessageProperties();
        messageProperties.setHeader("key1","value1");
        messageProperties.setHeader("key2","value2");
        Message obj=new Message(msg.getBytes(),messageProperties);
        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE,"",obj);
    }
    */
}
