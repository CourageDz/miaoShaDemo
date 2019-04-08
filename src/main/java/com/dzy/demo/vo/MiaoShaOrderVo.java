package com.dzy.demo.vo;

import com.dzy.demo.domain.MiaoShaUser;
import com.dzy.demo.domain.OrderInfo;

public class MiaoShaOrderVo {
    private OrderInfo orderInfo;
    private GoodsVo goods;

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "MiaoShaOrderVo{" +
                "orderInfo=" + orderInfo +
                ", goods=" + goods +
                '}';
    }
}
