package com.itmk.annotation;

import com.itmk.jwt.JwtUtils;
import com.itmk.web.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Auth annotation = null;
        if(handler instanceof HandlerMethod){
            annotation = ((HandlerMethod) handler).getMethodAnnotation(Auth.class);
        }else{
            return true;
        }
        //说明方法上面没有@Auth注解
        if(annotation == null){
             return true;
        }
        //获取token
        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token)){
            token = request.getParameter("token");
        }
        if(StringUtils.isEmpty(token)){
            throw new BusinessException(500,"token不能为空!");
        }
        //验证token
        jwtUtils.jwtDecode(token);
        return true;
    }
}
