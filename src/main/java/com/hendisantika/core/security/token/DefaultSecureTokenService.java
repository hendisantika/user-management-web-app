package com.hendisantika.core.security.token;

import com.hendisantika.core.security.jpa.SecureToken;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/04/22
 * Time: 05.32
 */
@Service
public class DefaultSecureTokenService implements SecureTokenService {

    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);
    private static final Charset US_ASCII = StandardCharsets.US_ASCII;
    @Autowired
    SecureTokenRepository secureTokenRepository;
    @Value("${jdj.secure.token.validity}")
    private int tokenValidityInSeconds;

    @Override
    public SecureToken createSecureToken() {
        String tokenValue = new String(Base64.encodeBase64URLSafe(DEFAULT_TOKEN_GENERATOR.generateKey()), US_ASCII);
        // this is a sample, you can adapt as per your security need
        SecureToken secureToken = new SecureToken();
        secureToken.setToken(tokenValue);
        secureToken.setExpireAt(LocalDateTime.now().plusSeconds(getTokenValidityInSeconds()));
        this.saveSecureToken(secureToken);
        return secureToken;
    }
}
