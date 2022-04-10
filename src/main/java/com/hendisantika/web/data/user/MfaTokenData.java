package com.hendisantika.web.data.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 11/04/22
 * Time: 05.42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MfaTokenData implements Serializable {
    private String qrCode;
    private String mfaCode;
}
