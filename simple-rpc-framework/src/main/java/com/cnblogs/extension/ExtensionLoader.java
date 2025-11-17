package com.cnblogs.extension;

import com.cnblogs.annotations.SPI;
import com.cnblogs.exceptions.FormatException;
import com.cnblogs.factory.SingletonFactory;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 加载SPI接口实现
 * @param <T>
 */
public class ExtensionLoader<T> {

    // 资源根目录
    private static final String RESOURCE_ROOT = "META-INF/extensions/";

    // Class类型
    private Class<T> type;

    // ExtensionLoader实例
    private static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();

    private final Map<String, Instance<T>> cachedInstances = new ConcurrentHashMap<>();


    private ExtensionLoader(Class<T> type) {
        this.type = type;
    }

    public static <S> ExtensionLoader<S> getExtensionLoader(Class<S> clz) {
        if(clz == null) {
            throw new IllegalArgumentException("clz should not be null");
        }
        if(!clz.isInterface()) {
            throw new IllegalArgumentException("clz should be interface");
        }
        if(!clz.isAnnotationPresent(SPI.class)) {
            throw new IllegalArgumentException("clz should be annotated by SPI");
        }
        ExtensionLoader<S> extensionLoader = (ExtensionLoader<S>) EXTENSION_LOADERS.get(clz);
        if(extensionLoader == null) {
            EXTENSION_LOADERS.putIfAbsent(clz, new ExtensionLoader<>(clz));
            extensionLoader = (ExtensionLoader<S>) EXTENSION_LOADERS.get(clz);
        }
        return extensionLoader;
    }

    public T getExtension(String extensionName) {
        if(StringUtils.isEmpty(extensionName)) {
            throw new IllegalArgumentException("extensionName should not be null");
        }
        Instance<T> instance = cachedInstances.get(extensionName);
        if(instance == null) {
            cachedInstances.putIfAbsent(extensionName, new Instance<>());
            instance = cachedInstances.get(extensionName);
        }
        T object = instance.get();
        if(object == null) {
            synchronized (instance) {
                object = instance.get(); // 需要再次获取，避免重复创建
                if(object == null) {
                    object = createExtension(extensionName);
                }
            }
        }
        return object;
    }

    private T createExtension(String extensionName) {
        if(type == null) {
            throw new IllegalArgumentException("type should not be null");
        }
        String resourcePath = RESOURCE_ROOT + type.getName();
        loadResources(resourcePath);
        Instance<T> instance = cachedInstances.get(extensionName);
        if(instance == null || instance.get() == null) {
            throw new RuntimeException("can not find extension: " + extensionName);
        }
        return instance.get();
    }

    private void loadResources(String resourcePath) {
        ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(resourcePath)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                int idx = line.indexOf('=');
                if(idx < 0) {
                    throw new FormatException("file format error");
                }
                String key = line.substring(0, idx).trim();
                String value = line.substring(idx + 1).trim();
                if(!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value) && !cachedInstances.containsKey(key)) {
                    Class<?> clz = classLoader.loadClass(value);
                    T obj = (T) SingletonFactory.getInstance(clz);
                    Instance<T> instance = cachedInstances.get(key);
                    if(instance == null) {
                        cachedInstances.putIfAbsent(key, new Instance<>());
                        instance = cachedInstances.get(key);
                    }
                    instance.set(obj);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("load extension file error", e);
        }
    }
}
