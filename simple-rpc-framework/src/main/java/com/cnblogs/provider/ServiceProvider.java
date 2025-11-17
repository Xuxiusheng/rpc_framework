package com.cnblogs.provider;

import com.cnblogs.config.RpcServiceConfig;

public interface ServiceProvider {
    void registry(RpcServiceConfig rpcServiceConfig);
}
