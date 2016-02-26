package com.snicesoft.basekit.net.api;

/**
 * Created by zhuzhe on 16/2/26.
 */
public final class API {
    static class Test {
        public static String scheme = Config.Scheme.HTTP;
        public static String ip = "0.0.0.0";
        public static String apiBaseName = "";
        public static int port = 0;
    }

    static class Product {
        public static String scheme = Config.Scheme.HTTP;
        public static String ip = "0.0.0.0";
        public static String apiBaseName = "";
        public static int port = 0;
    }

    public static Config config;

    private API() {
    }

    private static API instance = new API();

    public final static API getInstance() {
        return instance;
    }

    public API initProduct(String scheme, String ip, int port, String apiBaseName) {
        Product.scheme = scheme;
        Product.ip = ip;
        Product.port = port;
        Product.apiBaseName = apiBaseName;
        return this;
    }

    public API initTest(String scheme, String ip, int port, String apiBaseName) {
        Test.scheme = scheme;
        Test.ip = ip;
        Test.port = port;
        Test.apiBaseName = apiBaseName;
        return this;
    }

    public void init(ConfigFactory.Mode mode) {
        config = ConfigFactory.create(mode);
    }
}
