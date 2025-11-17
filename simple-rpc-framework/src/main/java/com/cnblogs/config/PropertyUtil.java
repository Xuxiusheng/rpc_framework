package com.cnblogs.config;


import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class PropertyUtil {

    public static Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new RuntimeException("rpc.properties file not found in classpath");
            }
            properties.load(inputStream);
        } catch(Exception e) {
            log.error("occur exception when load properties", e);
        }
        return properties;
    }
}
