package github.javaguide.factory;

import github.javaguide.extension.Holder;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SingletonFactory {
    private static Map<String, Holder<Object>> OBJECT_MAP = new ConcurrentHashMap<>();
    private static final Object lock = new Object();

    private SingletonFactory() {

    }

    public static <T> T getInstance(Class<T> clz) {
        if(clz == null) {
            throw new IllegalArgumentException("class cannot be null");
        }
        String key = clz.getName();

        // 1. 第一次检查：快速读取缓存（无锁）
        Holder<Object> holder = OBJECT_MAP.get(key);
        if (holder != null && holder.get() != null) {
            return clz.cast(holder.get());
        }

        // 2. 同步块：确保只有一个线程创建实例
        synchronized (lock) {
            // 3. 第二次检查：防止其他线程已创建holder
            holder = OBJECT_MAP.putIfAbsent(key, new Holder<Object>());

            // 4. 创建实例（此处不需要再次检查holder.get()，因为锁保证了互斥性）
            if (holder.get() == null) {
                try {
                    Constructor<T> constructor =  clz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    T instance = constructor.newInstance();
                    // 4.2 放入到map里面
                    holder.set(instance);
                } catch (Exception e) {
                    throw new RuntimeException("创建示例失败", e);
                }
            }
        }

        return clz.cast(holder.get());
    }
}
