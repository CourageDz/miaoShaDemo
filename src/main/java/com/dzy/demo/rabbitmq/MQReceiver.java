package com.dzy.demo.rabbitmq;

import com.dzy.demo.domain.MiaoShaOrder;
import com.dzy.demo.domain.MiaoShaUser;
import com.dzy.demo.service.GoodsService;
import com.dzy.demo.service.MiaoShaService;
import com.dzy.demo.service.RedisService;
import com.dzy.demo.vo.GoodsVo;
import com.dzy.demo.vo.MiaoShaInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {
    private Logger log= LoggerFactory.getLogger(MQReceiver.class);
    @Autowired
    GoodsService goodsService;

    @Autowired
    MiaoShaService miaoShaService;


    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void received(String message){
        log.info("MQReceiver"+message);
        MiaoShaInfo info=RedisService.stringToBean(message, MiaoShaInfo.class);
        //服务器判断库存
        MiaoShaUser user=info.getUser();
        long goodsId=info.getGoodsId();
        GoodsVo goodsVo=goodsService.getGoodsByGoodsId(goodsId);
        if(goodsVo.getStockCount()<=0)
            return;
        MiaoShaOrder order=miaoShaService.getMiaoShaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order!=null)
            return;
        miaoShaService.doMiaoSha(goodsVo,user);
    }
    /*
    @RabbitListener(queues = MQConfig.QUEUE)
    public void received(String message){
        log.info("MQReceiver received:"+message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void topic1Received(String message){
        log.info("TopicQueue1 Receiver received:"+message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void topic2Received(String message){
        log.info("TopicQueue2 Receiver received:"+message);
    }

    @RabbitListener(queues = MQConfig.HEADERS_QUEUE)
    public void headersReceived(byte[] message){
        log.info("HeadersQueue Receiver received:"+new String(message));
    }*/
}
