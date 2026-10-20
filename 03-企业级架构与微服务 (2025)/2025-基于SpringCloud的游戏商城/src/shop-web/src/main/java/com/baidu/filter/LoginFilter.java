package com.baidu.filter;

import com.baidu.pojo.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(filterName = "loginFilter",urlPatterns = {"/cart/addCart","/cart/showcartList","/toIndex","/order/buyDirect","/toUserIndex","/collect"})
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    //实现登录验证
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("--------doFilter---------");

        //将ServletRequest和ServletResponse对象向下转型
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        //获取HttpSession对象
        HttpSession session = request.getSession();

        //获取当前登录用户对象
        User user = (User) session.getAttribute("user1");

        //判断用户是否登录
        if(user!=null){//用户已经登录
            //请求向下传递
            filterChain.doFilter(request,response);
        }else {//用户没有登录，因为只有普通用户才能购买商品，所以直接重定向到普通用户的登录页面
            response.sendRedirect("/user/toUserLogin");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
