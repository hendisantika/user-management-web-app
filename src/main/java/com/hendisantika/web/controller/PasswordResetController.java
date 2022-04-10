package com.hendisantika.web.controller;

import com.hendisantika.core.exception.UnknownIdentifierException;
import com.hendisantika.core.service.CustomerAccountService;
import com.hendisantika.web.data.user.ResetPasswordData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/04/22
 * Time: 05.48
 */
@Controller
@RequestMapping("/password")
public class PasswordResetController {

    private static final String REDIRECT_LOGIN = "redirect:/login";
    private static final String MSG = "resetPasswordMsg";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private CustomerAccountService customerAccountService;

    @PostMapping("/request")
    public String resetPassword(final ResetPasswordData forgotPasswordForm, RedirectAttributes redirAttr) {
        try {
            customerAccountService.forgottenPassword(forgotPasswordForm.getEmail());
        } catch (UnknownIdentifierException e) {
            // log the error
        }
        redirAttr.addFlashAttribute(MSG,
                messageSource.getMessage("user.forgotpwd.msg", null, LocaleContextHolder.getLocale())
        );
        return REDIRECT_LOGIN;
    }
}
