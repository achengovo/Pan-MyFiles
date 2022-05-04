package com.yunpan.achengovo.interceptor;

import com.yunpan.achengovo.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class FileInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String url = request.getRequestURI();
        System.out.println(url);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("USER_SESSION");
        //判断Session中是否有用户数据，如果有继续向下执行
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/toLogin");
            return false;
        } else if (user != null) {
            //获取请求的URL
//            String url = request.getRequestURI();
            //URL:除了登录请求外，其他的URL都进行拦截控制
            if (url.indexOf("/upload") >= 0) {
                url = String.valueOf(url.split("/upload/")[1]);
                url = String.valueOf(url.split("/")[0]);
                String userId = String.valueOf(user.getUserId());
                if (userId.equals(url)) {
//                chain.doFilter(req, res);
                    System.out.println("通过");
                    return true;
                }
            }
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().print("<script>alert('您没有权限访问该页面！');</script>");
//        response.sendRedirect(request.getContextPath() + "/toLogin");
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
