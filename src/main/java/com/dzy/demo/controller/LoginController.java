package com.dzy.demo.controller;

import com.dzy.demo.result.Result;
import com.dzy.demo.service.MiaoShaUserService;
import com.dzy.demo.service.RedisService;
import com.dzy.demo.result.CodeMsg;
import com.dzy.demo.uitl.ValidatorUtil;
import com.dzy.demo.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {
    private static Logger log= LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MiaoShaUserService miaoShaUserService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> do_login(HttpServletResponse response,@Valid LoginVo vo){
        //登录
        miaoShaUserService.login(response,vo);
        return Result.success(true);
    }
}
