package github.javaguide.extension;

import github.javaguide.factory.SingletonFactory;
import github.javaguide.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class ExtensionLoader<T> {

    private static volatile Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();

    private static final String SERVICE_DIRECTORY = "META-INF/extensions/";

    // 对比一下private volatile Map<String, T> cachedInstances = new ConcurrentHashMap<>();
    private Map<String, Holder> cachedInstances = new ConcurrentHashMap<>();

    private Class<T> clz;
    private ExtensionLoader(Class<T> clz) {
        this.clz = clz;
    }

    public static <S> ExtensionLoader<S> getExtensionLoader(Class<S> clz) {
        if(clz == null) {
            throw new IllegalArgumentException("Extension type should not be null.");
        }
        if (!clz.isInterface()) {
            // 需要是接口
            throw new IllegalArgumentException("Extension type must be an interface.");
        }
        if (clz.getAnnotation(SPI.class) == null) {
            // 类上需要包含SPI注解
            throw new IllegalArgumentException("Extension type must be annotated by @SPI");
        }

        ExtensionLoader<S> extensionLoader = (ExtensionLoader<S>) EXTENSION_LOADERS.get(clz);
        if(extensionLoader == null) {
            EXTENSION_LOADERS.putIfAbsent(clz, new ExtensionLoader<S>(clz));
            extensionLoader = (ExtensionLoader<S>) EXTENSION_LOADERS.get(clz);
        }
        return extensionLoader;
    }

    public T getExtension(String name) {
        if(StringUtil.isBlank(name)) {
            throw new IllegalArgumentException("Extension name should not be null or empty.");
        }

        Holder holder = cachedInstances.get(name);
        if(holder == null) {
            cachedInstances.putIfAbsent(name, new Holder());
            holder = cachedInstances.get(name);
        }
        Object extension = holder.get();
        if(extension == null) {
            synchronized (holder) {
                extension = holder.get();
                if(extension == null) {
                    extension = initExtension(name);
                    if(extension == null) {
                        throw new RuntimeException("can not get extension instance by name " + name);
                    }
                    holder.set(extension);
                }
            }
        }
        return (T) extension;
    }

    private T initExtension(String name) {
        String fileName = ExtensionLoader.SERVICE_DIRECTORY + clz.getName();
        try {
            Enumeration<URL> urls;
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            urls = classLoader.getResources(fileName);
            if(urls != null) {
                while(urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    Object extension = loadExtension(name, url, classLoader);
                    if(extension != null) {
                        return (T) extension;
                    }
                }
            }
        } catch(IOException e) {
            log.error("load extension error", e);
        }
        return null;
    }

    private T loadExtension(String name, URL url, ClassLoader classLoader) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if(line.length() > 0) {
                    try {
                        final int ei = line.indexOf(':');
                        String extensionName = line.substring(0, ei).trim();
                        if(name.equals(extensionName)) {
                            String className = line.substring(ei + 1).trim();
                            Class<?> clz = classLoader.loadClass(className);
                            T extension = (T) SingletonFactory.getInstance(clz);
                            return extension;
                        }
                    } catch (ClassNotFoundException e) {
                        log.error("load extension class error", e);
                    }
                }
            }

        } catch (IOException e) {
            log.error("load extension error", e);
        }
        return null;
    }
}
