package com.hendisantika.core.security;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 10/04/22
 * Time: 13.42
 */
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    UserDetailsService userDetailsService;

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    private DataSource dataSource;

    @Resource
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Resource
    private CustomWebAuthenticationDetailsSource authenticationDetailsSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login", "/register", "/home")
                .permitAll()
                .antMatchers("/account/**").hasAnyAuthority("CUSTOMER", "ADMIN")
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and()
                //Setting HTTPS for my account
                .requiresChannel().anyRequest().requiresSecure()
                .and()
                // Remember me configurations
                .rememberMe().tokenRepository(persistentTokenRepository())
                .rememberMeCookieDomain("domain")
                .rememberMeCookieName("custom-remember-me-cookie")
                .userDetailsService(this.userDetailsService)
                .tokenValiditySeconds(2000)
                .useSecureCookie(true)

                //Login configurations
                .and()
                .formLogin()
                .defaultSuccessUrl("/account/home")
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .successHandler(successHandler())
                .failureHandler(failureHandler())
                .authenticationDetailsSource(authenticationDetailsSource) //injecting custom authenitcation source
                //logout configurations
                .and()
                .logout().deleteCookies("dummyCookie")
                .logoutSuccessUrl("/login");

                /*
                .and()
                .sessionManagement()
                .sessionFixation().none(); */

        http.authorizeRequests().antMatchers("/admim/**").hasAuthority("ADMIN");
        //http.addFilterAfter(customHeaderAuthFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    /* @Bean
    public CustomHeaderAuthFilter customHeaderAuthFilter(){
        return new CustomHeaderAuthFilter();
    } */


    private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource() {

        return new AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails>() {
            @Override
            public CustomWebAuthenticationDetails buildDetails(
                    HttpServletRequest request) {
                return new CustomWebAuthenticationDetails(request);
            }

        };
    }

    /**
     * <p></p>Creating bean for the custom suucess handler. You can use the custom success handlers for
     * different use cases like</p>
     * <li> Redirect customer to different page page based on profile.</li>
     * <li>Running some additional logic on post authentication before redirecting user</li>
     * <p>Check the <code>successHandler()</code> in the <code>configure</code> section for configuration details.</p>
     *
     * @return Customer success handler
     */
    @Bean
    public CustomSuccessHandler successHandler() {
        return new CustomSuccessHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

   /* @Bean
    public CustomAuthenticationFilter authFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationFailureHandler(failureHandler());
        filter.setAuthenticationSuccessHandler(successHandler());
        return filter;
    } */

    @Bean
    public LoginAuthenticationFailureHandler failureHandler() {
        LoginAuthenticationFailureHandler failureHandler = new LoginAuthenticationFailureHandler();
        failureHandler.setDefaultFailureUrl("/login?error=true");
        return failureHandler;
    }

    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher
            (ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }

    /**
     * We need this bean for the session management. Specially if we want to control the concurrent session-control
     * support
     * with Spring security.
     *
     * @return
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
