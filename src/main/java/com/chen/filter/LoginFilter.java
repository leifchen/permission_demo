package com.chen.filter;

import com.chen.common.RequestHolder;
import com.chen.model.SysUser;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录Filter
 * @Author LeifChen
 * @Date 2018-05-31
 */
@Slf4j
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        SysUser sysUser = (SysUser) httpRequest.getSession().getAttribute("user");
        if (sysUser == null) {
            String path = "/signin.jsp";
            httpResponse.sendRedirect(path);
            return;
        }
        RequestHolder.add(sysUser);
        RequestHolder.add(httpRequest);
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
