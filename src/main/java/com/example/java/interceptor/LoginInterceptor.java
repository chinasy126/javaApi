package com.example.java.interceptor;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.example.java.utils.JwtUtils;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Component
public class LoginInterceptor extends R implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "86400");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String path = request.getServletPath();

        String xtoken = request.getHeader("X-Token");
        System.out.println("访问前拦截请求，从请求头中获取token ---> " + xtoken);

        List<String> asList = Arrays.asList("/user/login" , "/image/**");

        String uri = request.getRequestURI();
        //1.设置放行路径
        if(asList.contains(uri)){
            return true;
        }

        if(StringUtils.isBlank(xtoken)){
            System.out.println("token为空，请携带正确的token");
           return false;
        }

        try {
            String username;
            username = JwtUtils.getClaimsByToken(xtoken).getSubject();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception {
//
//        if(!(handler instanceof HandlerMethod)){
//            return true;
//        }
//
//        System.out.println(request.getHeader("X-Token:")+"==========");
//        System.out.println(request.getHeader("token:")+"==========");
//        System.out.println("loginInterceptor");
//        return true;
//    }

}
