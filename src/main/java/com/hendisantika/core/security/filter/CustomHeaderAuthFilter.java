package com.hendisantika.core.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/04/22
 * Time: 05.25
 */
public class CustomHeaderAuthFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        var request = (HttpServletRequest) servletRequest;
        var response = (HttpServletResponse) servletResponse;
        filterChain.doFilter(servletRequest, servletResponse);
        //if header is missing , send un-athorized error back
        /*
        String authHeader = request.getHeader("X-HEADER");
        if(StringUtils.isEmpty(authHeader)){
            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED);
        }
        else{
            filterChain.doFilter(servletRequest, servletResponse);
        } */
    }
}
