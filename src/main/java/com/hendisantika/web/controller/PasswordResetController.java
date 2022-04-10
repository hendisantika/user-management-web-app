package com.hendisantika.web.controller;

import com.hendisantika.core.service.CustomerAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
