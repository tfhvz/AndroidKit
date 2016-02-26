package com.snicesoft.basekit.net.api;

/**
 * 正式环境接口配置
 *
 * @author zhuzhe
 */
class ProductConfig extends Config {
    @Override
    public String getScheme() {
        return API.Product.scheme;
    }

    @Override
    public String getIP() {
        return API.Product.ip;
    }

    @Override
    public String getApiBaseName() {
        return API.Product.apiBaseName;
    }

    @Override
    public int getPort() {
        return API.Product.port;
    }
}
