package com.snicesoft.net.api;

import com.snicesoft.basekit.net.api.Config;
import com.snicesoft.basekit.net.api.ConfigFactory;

public final class API {
    private static Config config;

    public final static class User {
        public static String USER;
        public static String USER_LOGIN;
    }

    private API() {
    }

    public static void init(ConfigFactory.Mode mode) {
        config = ConfigFactory.create(mode);
        User.USER = config.baseUrl() + "mobile/user";
        User.USER_LOGIN = config.baseUrl() + "mobile/user/login";
    }
}
