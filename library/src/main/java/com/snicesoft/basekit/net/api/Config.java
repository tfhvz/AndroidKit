package com.snicesoft.basekit.net.api;

public abstract class Config {
    public interface Scheme {
        String HTTP = "http://";
        String HTTPS = "https://";
    }

    public abstract String getScheme();

    public abstract String getIP();

    public abstract String getApiBaseName();

    public abstract int getPort();

    public final String baseUrl() {
        return APIUtils.getUrl(getScheme(), getIP(), getPort(), getApiBaseName());
    }
}
