package com.snicesoft.basekit.net.api;

/**
 * Created by zhuzhe on 15/10/10.
 */
public class APIConfig {
    public static void initTest(String scheme, String ip, int port, String apiBaseName) {
        Test.scheme = scheme;
        Test.ip = ip;
        Test.port = port;
        Test.apiBaseName = apiBaseName;
    }

    public static void initProduct(String scheme, String ip, int port, String apiBaseName) {
        Product.scheme = scheme;
        Product.ip = ip;
        Product.port = port;
        Product.apiBaseName = apiBaseName;
    }

    public static class Test {
        public static String scheme = Config.Scheme.HTTP;
        public static String ip = "0.0.0.0";
        public static String apiBaseName = "";
        public static int port = 0;
    }

    public static class Product {
        public static String scheme = Config.Scheme.HTTP;
        public static String ip = "0.0.0.0";
        public static String apiBaseName = "";
        public static int port = 0;
    }
}
