package com.hendisantika.web.controller;

import com.hendisantika.core.service.CustomerAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/04/22
 * Time: 05.46
 */
@Controller
@RequestMapping("/login")
public class LoginPageController {

    public static final String LAST_USERNAME_KEY = "LAST_USERNAME";

    @Resource(name = "customerAccountService")
    private CustomerAccountService customerAccountService;

}
