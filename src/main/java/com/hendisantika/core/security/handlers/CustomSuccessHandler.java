package com.hendisantika.core.security.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        if (isAdminAuthority(authentication)) {
            String targetUrl = "home/admin";
            clearAuthenticationAttributes(request);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {
            String targetUrl = determineTargetUrl(request, response); // using the super class to do this work for us

            // this logic is only for demo purpose, please do not use it on production application.
            if (StringUtils.isEmpty(targetUrl) || StringUtils.equals(targetUrl, "/")) {
                targetUrl = "/account/home";
            }
            clearAuthenticationAttributes(request);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);

            //You can let Spring security handle it for you.
            // super.onAuthenticationSuccess(request, response, authentication);
        }
    }


    protected boolean isAdminAuthority(final Authentication authentication) {
        return CollectionUtils.isNotEmpty(authentication.getAuthorities())
                && authentication.getAuthorities().contains(adminAuthority);
    }
}
