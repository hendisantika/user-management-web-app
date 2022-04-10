package com.hendisantika.core.security.mfa;

import dev.samstevens.totp.exceptions.QrGenerationException;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/04/22
 * Time: 05.30
 */
public interface MFATokenManager {
    String generateSecretKey();

    String getQRCode(final String secret) throws QrGenerationException;

    boolean verifyTotp(final String code, final String secret);
}
