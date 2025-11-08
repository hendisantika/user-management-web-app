package com.hendisantika.core.email.service;

import com.hendisantika.core.email.context.AbstractEmailContext;
import jakarta.mail.MessagingException;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/04/22
 * Time: 05.39
 */
public interface EmailService {

    void sendMail(final AbstractEmailContext email) throws MessagingException;
}
