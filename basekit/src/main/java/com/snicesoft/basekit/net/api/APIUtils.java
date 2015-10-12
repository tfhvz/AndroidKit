package com.snicesoft.basekit.net.api;

/**
 * Created by zhuzhe on 15/10/10.
 */
class APIUtils {
    public static String getUrl(String SCHEME, String IP, int PORT) {
        return SCHEME + IP + ((PORT == 80 || PORT <= 0) ? "" : ":" + PORT) + "/";
    }

    public static String getUrl(String SCHEME, String IP, int PORT, String apiBaseName) {
        return SCHEME + IP + ((PORT == 80 || PORT <= 0) ? "" : ":" + PORT) + "/" + apiBaseName;
    }
}