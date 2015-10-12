package com.snicesoft.basekit.net.api;

import com.snicesoft.basekit.LogKit;

public class ConfigFactory {
    public enum Mode {
        TEST, PRODUCT
    }

    private ConfigFactory() {
    }

    private static Config testServerConfig = new TestConfig();
    private static Config productServerConfig = new ProductConfig();

    public static Config create(Mode mode) {
        if (mode == Mode.TEST) {
            LogKit.d("API init [TEST].");
            return testServerConfig;
        } else if (mode == Mode.PRODUCT) {
            LogKit.d("API init [PRODUCT].");
            return productServerConfig;
        } else {
            return testServerConfig;
        }
    }
}
