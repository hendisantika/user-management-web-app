package com.hendisantika.core.security.userdetails;

import com.hendisantika.core.user.entity.Group;
import com.hendisantika.core.user.entity.UserEntity;
import com.hendisantika.core.user.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

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

    @Override
    public CustomUser loadUserByUsername(String email) throws UsernameNotFoundException {

        final UserEntity customer = userRepository.findByEmail(email);
        if (customer == null) {
            throw new UsernameNotFoundException(email);
        }
        boolean enabled = !customer.isAccountVerified(); // we can use this in case we want to activate account after
        // customer verified the account
        CustomUser user = CustomUser.CustomUserBuilder.aCustomUser().
                withUsername(customer.getEmail())
                .withPassword(customer.getPassword())
                .withEnabled(customer.isLoginDisabled())
                .withAuthorities(getAuthorities(customer))
                .withSecret(customer.getSecret())
                .withAccountNonLocked(false)
                .build();

        return user;
    }

    private Collection<GrantedAuthority> getAuthorities(UserEntity user) {
        Set<Group> userGroups = user.getUserGroups();
        Collection<GrantedAuthority> authorities = new ArrayList<>(userGroups.size());
        for (Group userGroup : userGroups) {
            authorities.add(new SimpleGrantedAuthority(userGroup.getCode().toUpperCase()));
        }

        return authorities;
    }

}
