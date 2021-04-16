package com.dyf.model;

import org.springframework.stereotype.Component;

/**
 * @Author: Ding Yongfei
 * @Date: 2020/3/1
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}
