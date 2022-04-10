package com.hendisantika.core.service;

import com.hendisantika.core.exception.InvalidTokenException;
import com.hendisantika.core.exception.UnknownIdentifierException;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 10/04/22
 * Time: 13.34
 */
public interface CustomerAccountService {
    void forgottenPassword(final String userName) throws UnknownIdentifierException;

    void updatePassword(final String password, final String token) throws InvalidTokenException,
            UnknownIdentifierException;

    boolean loginDisabled(final String username);
}
