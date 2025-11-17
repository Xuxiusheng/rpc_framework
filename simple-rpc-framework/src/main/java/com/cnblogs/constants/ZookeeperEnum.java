package com.cnblogs.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public enum ZookeeperEnum {
    ZK_CONFIG_PATH("rpc.properties"),
    ZK_ADDRESS("zookeeper.address");

    private final String propertyValue;
}
