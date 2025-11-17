package com.cnblogs.provider.registry;

import com.cnblogs.config.RpcServiceConfig;

import java.net.InetSocketAddress;

public interface Registry {
    void registry(RpcServiceConfig rpcServiceConfig, InetSocketAddress address);
}
