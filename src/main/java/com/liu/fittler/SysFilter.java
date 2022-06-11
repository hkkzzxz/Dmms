package com.liu.fittler;

import com.liu.pojo.User;
import com.liu.util.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SysFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest=(HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse=(HttpServletResponse) servletResponse;
        User user=(User) httpServletRequest.getSession().getAttribute(Constants.USER_SESSION);
        if (user==null)
        {
            System.out.println("没有用户");
            httpServletResponse.sendRedirect("/error.jsp");
        }
        else {
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
