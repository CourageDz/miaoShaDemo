package com.dzy.demo.config;

import com.dzy.demo.domain.MiaoShaUser;
import com.dzy.demo.service.MiaoShaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    MiaoShaUserService userService;
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?>clazz=methodParameter.getParameterType();
        return clazz== MiaoShaUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletResponse response=nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        HttpServletRequest request =nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String requestToken=request.getParameter(MiaoShaUserService.COOKIE_NAME_TOKEN);
        String cookieToken=getCookie(request,MiaoShaUserService.COOKIE_NAME_TOKEN);
        if(StringUtils.isEmpty(cookieToken)&& StringUtils.isEmpty(requestToken)){
            return null;
        }
        String token=StringUtils.isEmpty(requestToken)?cookieToken:requestToken;
        return userService.getByToken(response,token);
    }

    private String getCookie(HttpServletRequest request, String cookieNameToken) {
        Cookie[] cookies=request.getCookies();
        for (Cookie cookie:cookies) {
            if(cookie.getName().equals(cookieNameToken)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
