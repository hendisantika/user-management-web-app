package com.hendisantika.core.security.handlers;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/04/22
 * Time: 05.26
 */
public class CustomSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final GrantedAuthority adminAuthority = new SimpleGrantedAuthority(
            "ROLE_ADMIN");
}
