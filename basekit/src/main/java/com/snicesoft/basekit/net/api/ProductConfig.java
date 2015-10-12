package com.snicesoft.basekit.net.api;

/**
 * 正式环境接口配置
 *
 * @author zhuzhe
 */
class ProductConfig extends Config {
    @Override
    public String getScheme() {
        return APIConfig.Product.scheme;
    }

    @Override
    public String getIP() {
        return APIConfig.Product.ip;
    }

    @Override
    public String getApiBaseName() {
        return APIConfig.Product.apiBaseName;
    }

    @Override
    public int getPort() {
        return APIConfig.Product.port;
    }
}
