package com.dzy.demo.service;

import com.dzy.demo.controller.MiaoShaController;
import com.dzy.demo.dao.MiaoShaDao;
import com.dzy.demo.domain.MiaoShaOrder;
import com.dzy.demo.domain.MiaoShaUser;
import com.dzy.demo.domain.OrderInfo;
import com.dzy.demo.redis.MiaoShaKey;
import com.dzy.demo.redis.OrderKey;
import com.dzy.demo.result.Result;
import com.dzy.demo.uitl.MD5Util;
import com.dzy.demo.uitl.UUIDUtil;
import com.dzy.demo.vo.GoodsVo;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class MiaoShaService {
    private static Logger log= LoggerFactory.getLogger(MiaoShaService.class);


    @Resource
    MiaoShaDao miaoShaDao;

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;
    @Autowired
    RedisService redisService;


    public MiaoShaOrder getMiaoShaOrderByUserIdGoodsId(long goodsId, Long userId) {
//        return miaoShaDao.getMiaoShaOrderByUserIdGoodsId(goodsId, id);
        return redisService.get(OrderKey.ORDER_KEY_BY_UID_GID,""+userId+"_"+goodsId,MiaoShaOrder.class);
    }

    @Transactional
    public OrderInfo doMiaoSha(GoodsVo goodsVo, MiaoShaUser user) {
        boolean success=goodsService.decryGoodsCount(goodsVo);
        //判断库存是否小于0
        if (!success){
            setGoodsOver(goodsVo.getGoodsId());
            return null;
        }
        OrderInfo orderInfo=orderService.createOrder(goodsVo,user);
        orderService.createMiaoShaOrder(goodsVo,user,orderInfo);
        return orderInfo;
    }

    public long getMiaoShaResult(long goodsId, Long userId) {
        MiaoShaOrder miaoShaOrder=getMiaoShaOrderByUserIdGoodsId(goodsId,userId);
        boolean goodsOverSymbol=getGoodsOver(goodsId);
        if(miaoShaOrder!=null){
            log.info(miaoShaOrder.toString());
            return miaoShaOrder.getOrderId();
        }else if(!goodsOverSymbol){
            return 0;
        }else {
            return -1;
        }
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoShaKey.GOODS_OVER,""+goodsId);
    }

    private void setGoodsOver(long goodsId) {
        redisService.set(MiaoShaKey.GOODS_OVER,""+goodsId,true);
    }

    public boolean checkPath(String path, MiaoShaUser user, long goodsId) {
        if(user==null || path==null){
            return false;
        }
        String str=redisService.get(MiaoShaKey.GET_MIAOSHA_PATH,user.getId()+"_"+goodsId,String.class);
        return path.equals(str);
    }

    public String createMiaoShaPath(MiaoShaUser user, long goodsId) {
        String path=MD5Util.md5(UUIDUtil.uuid()+"12345");
        redisService.set(MiaoShaKey.GET_MIAOSHA_PATH,user.getId()+"_"+goodsId,path);
        return path;
    }

    public BufferedImage createVerifyCodeImg(MiaoShaUser user, long goodsId) {
        if(user == null || goodsId <=0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(MiaoShaKey.GET_MIAOSHA_VERIFY_CODE, user.getId()+","+goodsId, rnd);
        //输出图片
        return image;
    }

    private static char[] ops = new char[] {'+', '-', '*'};
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    private int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
