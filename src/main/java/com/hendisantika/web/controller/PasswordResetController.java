package com.hendisantika.web.controller;

import com.hendisantika.core.exception.InvalidTokenException;
import com.hendisantika.core.exception.UnknownIdentifierException;
import com.hendisantika.core.service.CustomerAccountService;
import com.hendisantika.web.data.user.ResetPasswordData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/change")
    public String changePassword(@RequestParam(required = false) String token, final RedirectAttributes redirAttr,
                                 final Model model) {
        if (StringUtils.isEmpty(token)) {
            redirAttr.addFlashAttribute("tokenError",
                    messageSource.getMessage("user.registration.verification.missing.token", null,
                            LocaleContextHolder.getLocale())
            );
            return REDIRECT_LOGIN;
        }

        ResetPasswordData data = new ResetPasswordData();
        data.setToken(token);
        setResetPasswordForm(model, data);

        return "/account/changePassword";
    }

    @PostMapping("/change")
    public String changePassword(final ResetPasswordData data, final Model model) {
        try {
            customerAccountService.updatePassword(data.getPassword(), data.getToken());
        } catch (InvalidTokenException | UnknownIdentifierException e) {
            // log error statement
            model.addAttribute("tokenError",
                    messageSource.getMessage("user.registration.verification.invalid.token", null,
                            LocaleContextHolder.getLocale())
            );

            return "/account/changePassword";
        }
        model.addAttribute("passwordUpdateMsg",
                messageSource.getMessage("user.password.updated.msg", null, LocaleContextHolder.getLocale())
        );
        setResetPasswordForm(model, new ResetPasswordData());
        return "/account/changePassword";
    }

    private void setResetPasswordForm(final Model model, ResetPasswordData data) {
        model.addAttribute("forgotPassword", data);
    }

}
