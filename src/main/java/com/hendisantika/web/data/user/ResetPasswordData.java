package com.hendisantika.web.data.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/04/22
 * Time: 05.43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordData {
    private String email;
    private String token;
    private String password;
    private String repeatPassword;
}
