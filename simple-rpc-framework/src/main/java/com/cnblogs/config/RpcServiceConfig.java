package com.cnblogs.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class RpcServiceConfig {

    /**
     * rpc接口版本
     */
    private String version;

    /**
     * service实现
     */
    private Object service;

    public String getRpcServiceName() {
        return getServiceName() + " " + version;
    }

    private String getServiceName() {
        return service.getClass().getInterfaces()[0].getCanonicalName();
    }
}
