package com.yunpan.achengovo.interceptor;
import com.yunpan.achengovo.entity.User;
import org.springframework.http.HttpCookie;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        //获取请求的URL
        String url = request.getRequestURI();
        System.out.println(url);
        System.out.println(url);
        //URL:除了登录请求外，其他的URL都进行拦截控制
        if (url.indexOf("/login") >= 0) {
            return true;
        }
        if (url.indexOf("/toLogin") >= 0) {
            return true;
        }
        if (url.indexOf("/register") >= 0) {
            return true;
        }
        if (url.indexOf("/toRegister") >= 0) {
            return true;
        }
        if (url.indexOf("/toForgetPass") >= 0) {
            return true;
        }
        if(url.indexOf("/forgetPassSendEmail")>=0){
            return true;
        }
        if(url.indexOf("/forgetPassCode")>=0){
            return true;
        }
        if(url.indexOf("forgetPassNewPass")>=0){
            return true;
        }
        //获取Session
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("ADMIN_SESSION");
        //判断Session中是否有用户数据，如果有，则返回true，继续向下执行
        if(user==null){
//            Cookie[] cookies = request.getCookies();
//            if(cookies != null && cookies.length > 0){
//                for (Cookie cookie : cookies){
//                    if(cookie.getName().equals("autoLoginCode")){
//                        response.sendRedirect(request.getContextPath()+"/loginByCookie");
//                    }
//                }
//            }
            user =(User) session.getAttribute("USER_SESSION");
        }
        if (user != null) {
            return true;
        }
        //不符合条件的给出提示信息，并转发到登录页面
        response.sendRedirect(request.getContextPath()+"/toLogin");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {

    }
}

