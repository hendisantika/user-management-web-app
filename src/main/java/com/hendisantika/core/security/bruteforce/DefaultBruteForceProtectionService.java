package com.hendisantika.core.security.bruteforce;

import com.hendisantika.core.user.entity.UserEntity;
import com.hendisantika.core.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 10/04/22
 * Time: 13.49
 */
@Service("bruteForceProtectionService")
public class DefaultBruteForceProtectionService implements BruteForceProtectionService {

    @Autowired
    UserRepository userRepository;
    @Value("${jdj.security.failedlogin.count}")
    private int maxFailedLogins;

    @Value("${jdj.brute.force.cache.max}")
    private int cacheMaxLimit;

    private final ConcurrentHashMap<String, FailedLogin> cache;

    public DefaultBruteForceProtectionService() {
        this.cache = new ConcurrentHashMap<>(cacheMaxLimit); //setting max limit for cache
    }

    @Override
    public void registerLoginFailure(String username) {

        UserEntity user = getUser(username);
        if (user != null && !user.isLoginDisabled()) {
            int failedCounter = user.getFailedLoginAttempts();
            if (maxFailedLogins < failedCounter + 1) {
                user.setLoginDisabled(true); //disabling the account
            } else {
                //let's update the counter
                user.setFailedLoginAttempts(failedCounter + 1);
            }
            userRepository.save(user);
        }
    }

    @Override
    public void resetBruteForceCounter(String username) {
        UserEntity user = getUser(username);
        if (user != null) {
            user.setFailedLoginAttempts(0);
            user.setLoginDisabled(false);
            userRepository.save(user);
        }
    }

    @Override
    public boolean isBruteForceAttack(String username) {
        UserEntity user = getUser(username);
        if (user != null) {
            return user.getFailedLoginAttempts() >= maxFailedLogins;
        }
        return false;
    }

    protected FailedLogin getFailedLogin(final String username) {
        FailedLogin failedLogin = cache.get(username.toLowerCase());

        if (failedLogin == null) {
            //setup the initial data
            failedLogin = new FailedLogin(0, LocalDateTime.now());
            cache.put(username.toLowerCase(), failedLogin);
            if (cache.size() > cacheMaxLimit) {

                // add the logic to remve the key based by timestamp
            }
        }
        return failedLogin;
    }

    private UserEntity getUser(final String username) {
        return userRepository.findByEmail(username);
    }

    public int getMaxFailedLogins() {
        return maxFailedLogins;
    }

}
