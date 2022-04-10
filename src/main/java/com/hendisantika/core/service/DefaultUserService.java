package com.hendisantika.core.service;

import com.hendisantika.core.exception.UserAlreadyExistException;
import com.hendisantika.core.user.entity.Group;
import com.hendisantika.core.user.entity.UserEntity;
import com.hendisantika.core.user.repository.UserGroupRepository;
import com.hendisantika.core.user.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 10/04/22
 * Time: 13.36
 */
@Service("userService")
public class DefaultUserService implements UserService {

    @Resource
    SecureTokenRepository secureTokenRepository;
    @Resource
    UserGroupRepository groupRepository;
    @Resource
    private UserRepository userRepository;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private EmailService emailService;
    @Resource
    private SecureTokenService secureTokenService;
    @Value("${site.base.url.https}")
    private String baseURL;

    @Resource
    private MFATokenManager mfaTokenManager;

    @Override
    public void register(UserData user) throws UserAlreadyExistException {
        if (checkIfUserExist(user.getEmail())) {
            throw new UserAlreadyExistException("User already exists for this email");
        }
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        encodePassword(user, userEntity);
        updateCustomerGroup(userEntity);
        userEntity.setSecret(mfaTokenManager.generateSecretKey());
        userRepository.save(userEntity);
        sendRegistrationConfirmationEmail(userEntity);
    }

    private void updateCustomerGroup(UserEntity userEntity) {
        Group group = groupRepository.findByCode("customer");
        userEntity.addUserGroups(group);
    }

    @Override
    public boolean checkIfUserExist(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
