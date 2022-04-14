package com.hendisantika.core.service;

import com.hendisantika.core.exception.InvalidTokenException;
import com.hendisantika.core.exception.UnknownIdentifierException;
import com.hendisantika.core.exception.UserAlreadyExistException;
import com.hendisantika.core.user.entity.UserEntity;
import com.hendisantika.web.data.user.MfaTokenData;
import com.hendisantika.web.data.user.UserData;
import dev.samstevens.totp.exceptions.QrGenerationException;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 10/04/22
 * Time: 13.39
 */
public interface UserService {
    void register(final UserData user) throws UserAlreadyExistException;

    boolean checkIfUserExist(final String email);

    void sendRegistrationConfirmationEmail(final UserEntity user);

    boolean verifyUser(final String token) throws InvalidTokenException;

    UserEntity getUserById(final String id) throws UnknownIdentifierException;

    MfaTokenData mfaSetup(final String email) throws UnknownIdentifierException, QrGenerationException;
}
