package com.snicesoft.basekit.net.api;

public abstract class Config {
    public interface Scheme {
        String HTTP = "http://";
        String HTTPS = "https://";
    }

    abstract String getScheme();

    abstract String getIP();

    abstract String getApiBaseName();

    abstract int getPort();

    public final String baseUrl() {
        return APIUtils.getUrl(getScheme(), getIP(), getPort(), getApiBaseName());
    }

    public final String schemeUrl() {
        return APIUtils.getUrl(getScheme(), getIP(), getPort());
    }
}
