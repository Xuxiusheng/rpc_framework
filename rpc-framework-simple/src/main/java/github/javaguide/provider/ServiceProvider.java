package github.javaguide.provider;

import github.javaguide.config.RpcServiceConfig;

public interface ServiceProvider {

    void addService(RpcServiceConfig rpcServiceConfig);

    Object getService(String rpcServiceName);

    void publishService(RpcServiceConfig rpcServiceConfig);
}
