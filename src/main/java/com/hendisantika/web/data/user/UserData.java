package com.hendisantika.web.data.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/04/22
 * Time: 05.44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData implements Serializable {

    @NotEmpty(message = "{registration.validation.firstName}")
    private String firstName;

    @NotEmpty(message = "{registration.validation.lastName}")
    private String lastName;

    @Email(message = "{registration.validation.email}")
    private String email;

    @NotEmpty(message = "{registration.validation.password}")
    private String password;

}
