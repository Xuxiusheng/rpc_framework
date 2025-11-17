package com.cnblogs.provider;

import com.cnblogs.annotations.SPI;
import com.cnblogs.config.RpcServiceConfig;

@SPI(type = "service provider")
public interface ServiceProvider {
    void addService(RpcServiceConfig rpcServiceConfig);

    Object getService(String rpcServiceName);

    boolean publishService(RpcServiceConfig rpcServiceConfig);
}
