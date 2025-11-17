package com.cnblogs.factory;

import com.cnblogs.extension.Instance;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SingletonFactory {

    private static final Map<String, Instance<Object>> cachedInstances = new HashMap<>();

    private SingletonFactory() {

    }

    public static <T> T getInstance(Class<T> clz) {
        if(clz == null) {
            throw new IllegalArgumentException("clz should not be null");
        }
        String key = clz.getName();
        Instance<Object> instance = cachedInstances.get(key);
        if(instance != null && instance.get() != null) {
            return clz.cast(instance.get());
        }

        synchronized (clz) {
            if(instance == null) {
                cachedInstances.putIfAbsent(key, new Instance<>());
                instance = cachedInstances.get(key);
            }

            if(instance.get() == null) {
                try {
                    T obj = clz.getDeclaredConstructor().newInstance();
                    instance.set(obj);
                    cachedInstances.put(key, instance);
                } catch (Exception e) {
                    throw new RuntimeException("创建单例失败", e);
                }
            }
        }
        return clz.cast(instance.get());
    }
}
