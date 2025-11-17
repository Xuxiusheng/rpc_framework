package com.cnblogs.provider.impl;

import com.cnblogs.config.RpcServiceConfig;
import com.cnblogs.factory.SingletonFactory;
import com.cnblogs.nettry.server.NettyServer;
import com.cnblogs.provider.ServiceProvider;
import com.cnblogs.provider.registry.Registry;
import com.cnblogs.provider.registry.impl.ZkRegistryImpl;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ZKServiceProviderImpl implements ServiceProvider {

    private Registry registry;
    private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    public ZKServiceProviderImpl() {
        this.registry = SingletonFactory.getInstance(ZkRegistryImpl.class);
    }

    @Override
    public void addService(RpcServiceConfig rpcServiceConfig) {
        String rpcServiceName = rpcServiceConfig.getRpcServiceName();
        if(serviceMap.containsKey(rpcServiceName)) {
            return;
        }

        serviceMap.putIfAbsent(rpcServiceName, rpcServiceConfig.getService());
        log.info("Add service: {} and interfaces:{}", rpcServiceName, rpcServiceConfig.getService().getClass().getInterfaces());
    }

    @Override
    public Object getService(String rpcServiceName) {
        if(!serviceMap.containsKey(rpcServiceName)) {
            throw new RuntimeException("can not find service: " + rpcServiceName);
        }
        return serviceMap.get(rpcServiceName);
    }

    @Override
    public boolean publishService(RpcServiceConfig rpcServiceConfig) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            InetSocketAddress address = new InetSocketAddress(host, NettyServer.PORT);
            registry.registry(rpcServiceConfig, address);
            addService(rpcServiceConfig);
        } catch (Exception e) {
            log.error("occur exception when getHostAddress", e);
            return false;
        }
        return true;
    }
}
