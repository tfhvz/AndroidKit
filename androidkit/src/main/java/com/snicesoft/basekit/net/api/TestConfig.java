package com.snicesoft.basekit.net.api;

class TestConfig extends Config {

    @Override
    public String getScheme() {
        return API.Test.scheme;
    }

    @Override
    public String getIP() {
        return API.Test.ip;
    }

    @Override
    public String getApiBaseName() {
        return API.Test.apiBaseName;
    }

    @Override
    public int getPort() {
        return API.Test.port;
    }
}