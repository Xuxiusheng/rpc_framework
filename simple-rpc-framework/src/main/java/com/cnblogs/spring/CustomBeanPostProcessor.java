package com.cnblogs.spring;

import com.cnblogs.annotations.RpcService;
import com.cnblogs.config.RpcServiceConfig;
import com.cnblogs.extension.ExtensionLoader;
import com.cnblogs.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomBeanPostProcessor implements BeanPostProcessor {
    private ServiceProvider serviceProvider;

    public CustomBeanPostProcessor() {
        serviceProvider = ExtensionLoader.getExtensionLoader(ServiceProvider.class).getExtension("ZKServiceProviderImpl");
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean.getClass().isAnnotationPresent(RpcService.class)) {
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            String version = rpcService.version();
            RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                    .version(version)
                    .service(bean)
                    .build();
            serviceProvider.publishService(rpcServiceConfig);
        }
        return bean;
    }
}
