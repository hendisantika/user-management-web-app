package com.hendisantika.core.security.bruteforce;

import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management-web-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 10/04/22
 * Time: 13.51
 */
public class FailedLogin {
    private int count;
    private LocalDateTime date;

    public FailedLogin() {
        this.count = 0;
        this.date = LocalDateTime.now();
    }

    public FailedLogin(int count, LocalDateTime date) {
        this.count = count;
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
