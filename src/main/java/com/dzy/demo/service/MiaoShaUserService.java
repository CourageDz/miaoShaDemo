package com.dzy.demo.service;

import com.dzy.demo.dao.MiaoshaUserDao;
import com.dzy.demo.domain.MiaoShaUser;
import com.dzy.demo.exception.GlobalException;
import com.dzy.demo.redis.MiaoShaUserPrefix;
import com.dzy.demo.result.CodeMsg;
import com.dzy.demo.uitl.MD5Util;
import com.dzy.demo.uitl.UUIDUtil;
import com.dzy.demo.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Service
public class MiaoShaUserService {
    private static Logger log= LoggerFactory.getLogger(MiaoShaUser.class);

    public static final String COOKIE_NAME_TOKEN="token";

    @Resource
    MiaoshaUserDao miaoshaUserDao;
    @Autowired
    RedisService redisService;

    public MiaoShaUser getByID(long id){
        MiaoShaUser user=redisService.get(MiaoShaUserPrefix.USER_ID,""+id,MiaoShaUser.class);
        if(user!=null)
            return user;
        user=miaoshaUserDao.getById(id);
        if(user!=null){
            redisService.set(MiaoShaUserPrefix.USER_ID,""+id,user);
        }
        return user;
    }

    public boolean updateUserPassword(String token,long id,String newPassword){
        MiaoShaUser user=miaoshaUserDao.getById(id);
        if(user==null)
            throw  new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        MiaoShaUser newUser=new MiaoShaUser();
        newUser.setId(id);
        newUser.setPassword(MD5Util.inputPassToDBPass(newPassword,user.getSalt()));
        miaoshaUserDao.updatePassword(newUser);
        //更新缓存
        redisService.delete(MiaoShaUserPrefix.USER_ID,""+id);
        user.setPassword(newUser.getPassword());
        redisService.set(MiaoShaUserPrefix.TOKEN,token,user);
        return true;
    }

    public MiaoShaUser getByToken(HttpServletResponse response,String token){
        if(StringUtils.isEmpty(token))
            return null;
        MiaoShaUser user=redisService.get(MiaoShaUserPrefix.TOKEN,token,MiaoShaUser.class);
        //延长token有效期
        addCookie(response,user,token);
        return user;
    }
    public boolean login(HttpServletResponse response,LoginVo loginVo) {
        if(loginVo==null){
            throw  new GlobalException(CodeMsg.SEVER_ERROR);
        }
        String mobile=loginVo.getMobile();
        String formPass=loginVo.getPassword();
        long userMobile=Long.parseLong(mobile);

        MiaoShaUser miaoShaUser=getByID(userMobile);
        if(miaoShaUser==null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        String dbPass=miaoShaUser.getPassword();
        String saltDB=miaoShaUser.getSalt();
        String calcPass=MD5Util.formPassToDBPass(formPass,saltDB);
        if(!calcPass.equals(dbPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        String token=UUIDUtil.uuid();
        addCookie(response,miaoShaUser,token);
        return true;
    }

    private void addCookie(HttpServletResponse response,MiaoShaUser miaoShaUser,String token){
        redisService.set(MiaoShaUserPrefix.TOKEN,token,miaoShaUser);
        Cookie cookie =new Cookie(COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(MiaoShaUserPrefix.MIAOSHAUSER_EXPIRE);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
