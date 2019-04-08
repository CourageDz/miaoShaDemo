package com.dzy.demo.service;

import com.dzy.demo.controller.MiaoShaController;
import com.dzy.demo.dao.OrderDao;
import com.dzy.demo.domain.Goods;
import com.dzy.demo.domain.MiaoShaOrder;
import com.dzy.demo.domain.MiaoShaUser;
import com.dzy.demo.domain.OrderInfo;
import com.dzy.demo.redis.OrderKey;
import com.dzy.demo.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class OrderService {
    private static Logger log= LoggerFactory.getLogger(OrderService.class);
    @Resource
    OrderDao orderDao;

    @Autowired
    RedisService redisService;

    public OrderInfo createOrder(GoodsVo goods, MiaoShaUser user) {
        OrderInfo order=new OrderInfo();
        order.setCreateDate(new Date());
        order.setGoodsId(goods.getId());
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsPrice(goods.getMiaoShaPrice());
        order.setDeliveryAddrId(0L);
        order.setGoodsCount(1);
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setUserId(user.getId());
        orderDao.createOrder(order);
        log.info(order.toString());
        return order;
    }

    public void createMiaoShaOrder(GoodsVo goodsVo, MiaoShaUser user,OrderInfo orderInfo) {
        MiaoShaOrder order=new MiaoShaOrder();
        order.setUserId(user.getId());
        order.setGoodsId(goodsVo.getId());
        order.setOrderId(orderInfo.getId());
        orderDao.createMiaoShaOrder(order);
        redisService.set(OrderKey.ORDER_KEY_BY_UID_GID,""+user.getId()+"_"+goodsVo.getGoodsId(),order);
        log.info(order.toString());
    }

    public OrderInfo getOrderByOrderId(long orderId) {
        return orderDao.getOrderByOrderId(orderId);
    }

}
