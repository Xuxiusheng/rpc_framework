package com.cnblogs.utils;

import com.cnblogs.config.PropertyUtil;
import com.cnblogs.constants.ZookeeperEnum;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Properties;

public class CuratorUtil {

    private static CuratorFramework zkClient;

    // zk默认地址
    private static final String DEFAULT_ZOOKEEPER_ADDRESS = "127.0.0.1:2181";

    private CuratorUtil() {
    }

    public static CuratorFramework getZkClient() {
        if(zkClient != null && zkClient.getState() == CuratorFrameworkState.STARTED) {
            return zkClient;
        }
        String fileName = ZookeeperEnum.ZK_CONFIG_PATH.getPropertyValue();
        Properties properties = PropertyUtil.loadProperties(fileName);
        String zkAddress = DEFAULT_ZOOKEEPER_ADDRESS;
        if(properties != null && properties.getProperty(ZookeeperEnum.ZK_ADDRESS.getPropertyValue()) != null) {
            zkAddress = properties.getProperty(ZookeeperEnum.ZK_ADDRESS.getPropertyValue());
        }

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        zkClient = CuratorFrameworkFactory.builder()
                // the server to connect to (can be a server list)
                .connectString(zookeeperAddress)
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
    }
}
