package com.hendisantika.core.security.userdetails;

import com.hendisantika.core.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/04/22
 * Time: 05.21
 */
@Service("userDetailsService")
@Transactional
public class CustomUserDetailService implements UserDetailsService {

    @Resource
    UserRepository userRepository;
}
