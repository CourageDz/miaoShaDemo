package com.dzy.demo.controller;

import com.dzy.demo.domain.User;
import com.dzy.demo.rabbitmq.MQSender;
import com.dzy.demo.service.RedisService;
import com.dzy.demo.redis.UserKey;
import com.dzy.demo.result.CodeMsg;
import com.dzy.demo.result.Result;
import com.dzy.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/demo")
public class HelloController {


    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender mqSender;

    /*@RequestMapping("/mqTest")
    @ResponseBody
    public Result<String> mQTest(){
        mqSender.send("hello rabbitMQ");
        return Result.success("hello MQ");
    }

    @RequestMapping("/mq/topic")
    @ResponseBody
    public Result<String> topicQueueTest(){
        mqSender.sendTopic("hello TopicQueue");
        return Result.success("hello MQ");
    }

    @RequestMapping("/mq/fanout")
    @ResponseBody
    public Result<String> fanoutQueueTest(){
        mqSender.sendFanout("hello fanoutQueue");
        return Result.success("hello MQ");
    }

    @RequestMapping("/mq/headers")
    @ResponseBody
    public Result<String> headersQueueTest(){
        mqSender.sendHeaders("hello headersQueue");
        return Result.success("hello MQ");
    }*/

    @RequestMapping("/dd")
    public String say(){
        return "hello";
    }
    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello(){
        return Result.success("hello Java");
    }

    @RequestMapping("/helloError")
    @ResponseBody
    public Result<String> helloError(){
        return Result.error(CodeMsg.SEVER_ERROR);
    }


    @RequestMapping("/thymeleaf")
    public String thymeleaf(){
        ModelAndView model =new ModelAndView("items");
        model.addObject("name","dzy");
        model.addObject("nickName","asdfasdfa");
        return "hello";
    }

    @RequestMapping("/thymeleaf2")
    public String thymeleaf2(Model model){
        model.addAttribute("name","dzy");
        model.addAttribute("nickName","asdfasdfa");
        return "hello";
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> getById(){
        User user=userService.getById(2);
        return  Result.success(user);
    }

    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx(){
        boolean bool=userService.tx();
        return Result.success(bool);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<Long> redisGet(){
        Long v1=redisService.get(UserKey.getById,"key1",Long.class);
        return Result.success(v1);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<String> redisSet(){
        boolean res=redisService.set(UserKey.getById,"key2","hello redis");
        String str=redisService.get(UserKey.getById,"key2",String.class);
        return Result.success(str);
    }




}
