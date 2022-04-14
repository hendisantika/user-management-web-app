package com.hendisantika.core.security.authentication;

import com.hendisantika.core.security.mfa.MFATokenManager;
import com.hendisantika.core.security.userdetails.CustomUser;
import com.hendisantika.core.security.web.authentication.CustomWebAuthenticationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/04/22
 * Time: 05.19
 */
@Component
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Resource
    private MFATokenManager mfaTokenManager;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Autowired
    public CustomAuthenticationProvider(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
    }

    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {

        super.additionalAuthenticationChecks(userDetails, authentication);

        //token check
        CustomWebAuthenticationDetails authenticationDetails =
                (CustomWebAuthenticationDetails) authentication.getDetails();
        CustomUser user = (CustomUser) userDetails;
        String mfaToken = authenticationDetails.getToken();
        if (!mfaTokenManager.verifyTotp(mfaToken, user.getSecret())) {
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
    }
}
