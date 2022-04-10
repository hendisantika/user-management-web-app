package com.hendisantika.core.security.filter;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/04/22
 * Time: 05.23
 */
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String customToken = "jdjCustomToken";
}
