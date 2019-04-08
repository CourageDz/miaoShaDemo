package com.dzy.demo.controller;

import com.dzy.demo.domain.MiaoShaOrder;
import com.dzy.demo.domain.MiaoShaUser;
import com.dzy.demo.redis.GoodsKey;
import com.dzy.demo.result.Result;
import com.dzy.demo.service.GoodsService;
import com.dzy.demo.service.MiaoShaUserService;
import com.dzy.demo.service.RedisService;
import com.dzy.demo.vo.GoodsDetailVo;
import com.dzy.demo.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.context.SpringContextUtils;
import org.thymeleaf.spring5.context.webflux.SpringWebFluxContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    private static Logger log= LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;


    @RequestMapping(value = "/to_list",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String toList(HttpServletRequest request,HttpServletResponse response, Model model, MiaoShaUser user){
        model.addAttribute("user",user);

//        return "goods_list";
        String html=redisService.get(GoodsKey.GOODS_LIST,"",String.class);
        if(!StringUtils.isEmpty(html))
            return html;

        List<GoodsVo> miaoShaGoods=goodsService.listMiaoShaGoods();
        model.addAttribute("goodsList",miaoShaGoods);

        //手动渲染
        WebContext ctx=new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html=thymeleafViewResolver.getTemplateEngine().process("goods_list",ctx);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.GOODS_LIST,"",html);
        }
        return html;
    }

    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(MiaoShaUser user, @PathVariable("goodsId") long goodsId){

        GoodsVo goods=goodsService.getGoodsByGoodsId(goodsId);

        long startTime=goods.getStartDate().getTime();
        long endTime=goods.getEndDate().getTime();
        long currentTime=System.currentTimeMillis();
        int miaoShaStatus;
        int remainSeconds;
        if(currentTime<startTime){
            miaoShaStatus=0;
            remainSeconds=(int)(startTime-currentTime)/1000;
        }else if(currentTime>endTime){
            miaoShaStatus=2;
            remainSeconds=-1;
        }else {
            miaoShaStatus=1;
            remainSeconds=0;
        }

        GoodsDetailVo goodsDetailVo=new GoodsDetailVo();
        goodsDetailVo.setGoods(goods);
        goodsDetailVo.setMiaoShaStatus(miaoShaStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        goodsDetailVo.setUser(user);
       return Result.success(goodsDetailVo);
    }

    @RequestMapping(value = "/to_detail2/{goodsId}",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String detail2(HttpServletRequest request,HttpServletResponse response, Model model, MiaoShaUser user, @PathVariable("goodsId") long goodsId){
        model.addAttribute("user",user);

        String html=redisService.get(GoodsKey.GOODS_DETAIL,""+goodsId,String.class);
        if(!StringUtils.isEmpty(html))
            return html;

        GoodsVo goods=goodsService.getGoodsByGoodsId(goodsId);
        model.addAttribute("goods",goods);

        long startTime=goods.getStartDate().getTime();
        long endTime=goods.getEndDate().getTime();
        long currentTime=System.currentTimeMillis();

        int miaoShaStatus;
        int remainSeconds;
        if(currentTime<startTime){
            miaoShaStatus=0;
            remainSeconds=(int)(startTime-currentTime)/1000;
        }else if(currentTime>endTime){
            miaoShaStatus=2;
            remainSeconds=-1;
        }else {
            miaoShaStatus=1;
            remainSeconds=0;
        }
        model.addAttribute("miaoShaStatus",miaoShaStatus);
        model.addAttribute("remainSeconds",remainSeconds);

//        return "goods_detail";
        WebContext ctx=new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html=thymeleafViewResolver.getTemplateEngine().process("goods_detail",ctx);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.GOODS_DETAIL,""+goodsId,html);
        }
        return html;
    }

}
