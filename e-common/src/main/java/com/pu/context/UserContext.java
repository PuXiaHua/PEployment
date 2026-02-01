package com.pu.context;

import com.pu.epojo.User;

public class UserContext {
    private final static ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static User getUser() {
        return threadLocal.get();
    }

    public static void setUser(User user) {
        threadLocal.set(user);
    }

    public static void removeUser() {
        threadLocal.remove();
    }
}
