package com.hendisantika.core.security.token;

import com.hendisantika.core.security.jpa.SecureToken;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/04/22
 * Time: 05.31
 */
public interface SecureTokenService {
    SecureToken createSecureToken();

    void saveSecureToken(final SecureToken token);

    SecureToken findByToken(final String token);

    void removeToken(final SecureToken token);

    void removeTokenByToken(final String token);
}
