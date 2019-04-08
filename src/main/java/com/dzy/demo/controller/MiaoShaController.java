package com.dzy.demo.controller;

import com.dzy.demo.domain.MiaoShaOrder;
import com.dzy.demo.domain.MiaoShaUser;
import com.dzy.demo.domain.OrderInfo;
import com.dzy.demo.rabbitmq.MQSender;
import com.dzy.demo.redis.GoodsKey;
import com.dzy.demo.redis.MiaoShaKey;
import com.dzy.demo.result.CodeMsg;
import com.dzy.demo.result.Result;
import com.dzy.demo.service.GoodsService;
import com.dzy.demo.service.MiaoShaService;
import com.dzy.demo.service.RedisService;
import com.dzy.demo.uitl.MD5Util;
import com.dzy.demo.uitl.UUIDUtil;
import com.dzy.demo.vo.GoodsVo;
import com.dzy.demo.vo.MiaoShaInfo;
import com.sun.org.apache.bcel.internal.classfile.Code;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/miaosha")
public class MiaoShaController implements InitializingBean {
    private static Logger log= LoggerFactory.getLogger(MiaoShaController.class);

    @Autowired
    GoodsService goodsService;

    @Autowired
    MiaoShaService miaoShaService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender miaoShaSender;

    Map<Long,Boolean>localOverMap=new HashMap<>();

    //初始化，设置Goods库存的缓存
    @Override
    public void afterPropertiesSet(){
        List<GoodsVo>goodsList= goodsService.listMiaoShaGoods();
        if(goodsList==null)
            return;
        for (GoodsVo goods:goodsList){
            log.info(goods.toString());
            redisService.set(GoodsKey.GOODS_COUNT,""+goods.getGoodsId(),goods.getStockCount());
            localOverMap.put(goods.getGoodsId(),false);
        }
    }

    /***
     * @return -1表示商品已抢完，0表示还在排队 ，订单号表示抢购成功
     */
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> getResult(Model model,MiaoShaUser user, @RequestParam("goodsId") long goodsId){
        log.info("get Result: "+new Date());
        model.addAttribute("user",user);
        if(user==null)
            return Result.error(CodeMsg.SESSION_ERROR);
        long result=miaoShaService.getMiaoShaResult(goodsId,user.getId());
        return Result.success(result);
    }

    @RequestMapping("/do_miaosha2")
    public String doMiaoSha2(Model model, MiaoShaUser user, @RequestParam("goodsId") long goodsId){
        GoodsVo goodsVo=goodsService.getGoodsByGoodsId(goodsId);
        if(goodsVo.getStockCount()<0){
            model.addAttribute("errmsg", CodeMsg.GOODS_OVER.getMsg());
            return "miaosha_failed";
        }
        MiaoShaOrder order=miaoShaService.getMiaoShaOrderByUserIdGoodsId(goodsId,user.getId());
        if(order!=null){
            model.addAttribute("errmsg", CodeMsg.REPEAT_MIAOSHA.getMsg());
            return "miaosha_failed";
        }
        OrderInfo orderInfo=miaoShaService.doMiaoSha(goodsVo,user);
        model.addAttribute("order", orderInfo);
        model.addAttribute("goods", goodsVo);
        return "order_detail";
    }
    @RequestMapping(value = "/getPath",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoShaPath(MiaoShaUser user, @RequestParam("goodsId") long goodsId){
        if(user==null)
            return Result.error(CodeMsg.SESSION_ERROR);
        String path=miaoShaService.createMiaoShaPath(user,goodsId);
        return Result.success(path);
    }

    @RequestMapping(value = "/getVerifyCodeImd",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoShaPath(HttpServletResponse response,MiaoShaUser user, @RequestParam("goodsId") long goodsId){
        if(user==null )
            return Result.error(CodeMsg.SESSION_ERROR);
        try{
            BufferedImage image=miaoShaService.createVerifyCodeImg(user,goodsId);
            OutputStream out=response.getOutputStream();
            ImageIO.write(image,"JPEG",out);
            out.flush();
            out.close();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAILED);
        }
    }

    @RequestMapping(value = "/{path}/do_miaosha", method= RequestMethod.POST)
    @ResponseBody
    public Result<Integer> doMiaoSha(Model model, MiaoShaUser user, @RequestParam("goodsId") long goodsId, @PathVariable("path") String path){
        model.addAttribute("user", user);
        if(user==null)
            return Result.error(CodeMsg.SESSION_ERROR);

        boolean ifPathValid=miaoShaService.checkPath(path,user,goodsId);
        if(!ifPathValid)
            return Result.error(CodeMsg.MIAOSHA_PATH_ERROR);
        boolean over=localOverMap.get(goodsId);
        if(over)
            return Result.error(CodeMsg.GOODS_OVER);
        //预减库存
        long goodsCount=redisService.decr(GoodsKey.GOODS_COUNT,""+goodsId);
        if(goodsCount<0){
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.GOODS_OVER);
        }

        //判断是否重复秒杀
        MiaoShaOrder order=miaoShaService.getMiaoShaOrderByUserIdGoodsId(goodsId,user.getId());
        if(order!=null){
            return Result.error(CodeMsg.REPEAT_MIAOSHA);
        }

        //入队
        MiaoShaInfo info=new MiaoShaInfo();
        info.setGoodsId(goodsId);
        info.setUser(user);
        log.info(info.toString());
        miaoShaSender.sendMiaoShaRequest(info);
        return Result.success(0);

        /*GoodsVo goodsVo=goodsService.getGoodsByGoodsId(goodsId);

        if(goodsVo.getStockCount()<0){
            return Result.error(CodeMsg.GOODS_OVER);
        }
        MiaoShaOrder order=miaoShaService.getMiaoShaOrderByUserIdGoodsId(goodsId,user.getId());
        if(order!=null){
            return Result.error(CodeMsg.REPEAT_MIAOSHA);
        }
        OrderInfo orderInfo=miaoShaService.doMiaoSha(goodsVo,user);
        return Result.success(orderInfo);*/
    }


}
