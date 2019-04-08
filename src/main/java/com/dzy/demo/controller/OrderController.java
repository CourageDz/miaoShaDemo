package com.dzy.demo.controller;

import com.dzy.demo.domain.Goods;
import com.dzy.demo.domain.MiaoShaUser;
import com.dzy.demo.domain.OrderInfo;
import com.dzy.demo.result.CodeMsg;
import com.dzy.demo.result.Result;
import com.dzy.demo.service.GoodsService;
import com.dzy.demo.service.OrderService;
import com.dzy.demo.service.RedisService;
import com.dzy.demo.vo.GoodsVo;
import com.dzy.demo.vo.MiaoShaOrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping(value = "/getOrder")
    @ResponseBody
    public Result<MiaoShaOrderVo> getMiaoShaOrder(Model model, MiaoShaUser user, @RequestParam("orderId") long orderId){
        if(user==null)
            return Result.error(CodeMsg.MOBILE_NOT_EXIST);
        OrderInfo orderInfo=orderService.getOrderByOrderId(orderId);
        if(orderInfo==null)
            return Result.error(CodeMsg.ORDER_NOT_EXIST);

        long goodsId=orderInfo.getGoodsId();
        GoodsVo goodsVo=goodsService.getGoodsByGoodsId(goodsId);
        if(goodsVo==null)
            return Result.error(CodeMsg.GOODS_OVER);

        MiaoShaOrderVo miaoShaOrderVo=new MiaoShaOrderVo();
        miaoShaOrderVo.setOrderInfo(orderInfo);
        miaoShaOrderVo.setGoods(goodsVo);
       return Result.success(miaoShaOrderVo);
    }

}
