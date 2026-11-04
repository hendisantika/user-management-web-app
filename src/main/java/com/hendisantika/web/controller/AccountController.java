package com.hendisantika.web.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/04/22
 * Time: 05.44
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    @Resource
    private SessionRegistry sessionRegistry;

    @Resource
    private MessageSource messageSource;

    @GetMapping("/home")
    public String home(HttpServletResponse response, Authentication authentication, HttpSession httpSession,
                       Model model) {
        setDummyCookie(response);
        List<SessionInformation> sessions = sessionRegistry.getAllSessions(authentication.getPrincipal(), false)
                .stream()
                .sorted(Comparator.comparing(SessionInformation::getLastRequest).reversed())
                .toList();
        model.addAttribute("currentSession", sessions);
        model.addAttribute("currentSessionId", httpSession.getId());
        return "index";
    }

    /**
     * Logs out (expires) one of the current user's other sessions.
     */
    @PostMapping("/sessions/{sessionId}/logout")
    public String logoutSession(@PathVariable String sessionId, Authentication authentication,
                                HttpSession httpSession, RedirectAttributes redirAttr) {
        if (!sessionId.equals(httpSession.getId())) {
            sessionRegistry.getAllSessions(authentication.getPrincipal(), false).stream()
                    .filter(info -> info.getSessionId().equals(sessionId))
                    .findFirst()
                    .ifPresent(info -> {
                        info.expireNow();
                        redirAttr.addFlashAttribute("sessionMsg", messageSource.getMessage(
                                "user.session.logout.success", null, LocaleContextHolder.getLocale()));
                    });
        }
        return "redirect:/account/home";
    }

    private void setDummyCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("dummyCookie", "dummy_cookie");
        cookie.setMaxAge(2592000);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
