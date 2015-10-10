package com.snicesoft.basekit.net.api;

class TestConfig extends Config {

    @Override
    public String getScheme() {
        return APIConfig.Test.scheme;
    }

    @Override
    public String getIP() {
        return APIConfig.Test.ip;
    }

    @Override
    public String getApiBaseName() {
        return APIConfig.Test.apiBaseName;
    }

    @Override
    public int getPort() {
        return APIConfig.Test.port;
    }
}
