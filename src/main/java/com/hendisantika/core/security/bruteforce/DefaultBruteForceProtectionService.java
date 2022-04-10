package com.hendisantika.core.security.bruteforce;

import com.hendisantika.core.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
