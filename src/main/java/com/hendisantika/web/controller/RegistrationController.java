package com.hendisantika.web.controller;

import com.hendisantika.core.exception.InvalidTokenException;
import com.hendisantika.core.exception.UnknownIdentifierException;
import com.hendisantika.core.exception.UserAlreadyExistException;
import com.hendisantika.core.service.UserService;
import com.hendisantika.web.data.user.MfaTokenData;
import com.hendisantika.web.data.user.UserData;
import dev.samstevens.totp.exceptions.QrGenerationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
 * Link : s.id/hendisantika
 * Date: 04/11/26
 * Time: 06.47
 */
@Slf4j
@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {

    private static final String REGISTER_VIEW = "account/register";
    private static final String REDIRECT_LOGIN = "redirect:/login";

    private final UserService userService;
    private final MessageSource messageSource;

    @GetMapping
    public String register(final Model model) {
        model.addAttribute("userData", new UserData());
        return REGISTER_VIEW;
    }

    @PostMapping
    public String userRegistration(@Valid @ModelAttribute("userData") final UserData userData,
                                   final BindingResult bindingResult, final Model model) {
        if (bindingResult.hasErrors()) {
            return REGISTER_VIEW;
        }
        try {
            userService.register(userData);
            MfaTokenData mfaData = userService.mfaSetup(userData.getEmail());
            model.addAttribute("qrCode", mfaData.getQrCode());
            model.addAttribute("qrCodeKey", mfaData.getMfaCode());
        } catch (UserAlreadyExistException e) {
            bindingResult.rejectValue("email", "userData.email",
                    getMessage("user.registration.email.exists"));
            return REGISTER_VIEW;
        } catch (UnknownIdentifierException | QrGenerationException e) {
            log.error("Unable to generate MFA QR code for {}", userData.getEmail(), e);
        }
        model.addAttribute("registrationMsg", getMessage("user.registration.verification.email.msg"));
        return REGISTER_VIEW;
    }

    @GetMapping("/verify")
    public String verifyCustomer(@RequestParam(required = false) final String token,
                                 final RedirectAttributes redirAttr) {
        if (StringUtils.isEmpty(token)) {
            redirAttr.addFlashAttribute("tokenError", getMessage("user.registration.verification.missing.token"));
            return REDIRECT_LOGIN;
        }
        try {
            userService.verifyUser(token);
        } catch (InvalidTokenException e) {
            redirAttr.addFlashAttribute("tokenError", getMessage("user.registration.verification.invalid.token"));
            return REDIRECT_LOGIN;
        }
        redirAttr.addFlashAttribute("verifiedAccountMsg", getMessage("user.registration.verification.success"));
        return REDIRECT_LOGIN;
    }

    private String getMessage(final String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }
}
