package com.hendisantika.core.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/04/22
 * Time: 05.34
 */
public class CustomUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final String customToken;

    public CustomUsernamePasswordAuthenticationToken(Object principal, Object credentials, String customToken) {
        super(principal, credentials);
        this.customToken = customToken;
    }
}
