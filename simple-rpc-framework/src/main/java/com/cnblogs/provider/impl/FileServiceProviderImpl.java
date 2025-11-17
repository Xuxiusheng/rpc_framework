package com.cnblogs.provider.impl;

import com.cnblogs.config.RpcServiceConfig;
import com.cnblogs.provider.ServiceProvider;

public class FileServiceProviderImpl implements ServiceProvider {

    @Override
    public void addService(RpcServiceConfig rpcServiceConfig) {

    }

    @Override
    public Object getService(String rpcServiceName) {
        return null;
    }

    @Override
    public boolean publishService(RpcServiceConfig rpcServiceConfig) {
        return false;
    }
}
