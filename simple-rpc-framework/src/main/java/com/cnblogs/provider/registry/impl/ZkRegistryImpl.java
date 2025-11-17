package com.cnblogs.provider.registry.impl;

import com.cnblogs.config.RpcServiceConfig;
import com.cnblogs.provider.registry.Registry;

import java.net.InetSocketAddress;

public class ZkRegistryImpl implements Registry {

    @Override
    public void registry(RpcServiceConfig rpcServiceConfig, InetSocketAddress address) {
        // Implementation for registering the service with Zookeeper would go here
    }
}
