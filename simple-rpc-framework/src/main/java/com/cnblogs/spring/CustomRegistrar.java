package com.cnblogs.spring;

import com.cnblogs.annotations.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.Map;

@Slf4j
public class CustomRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(CustomRegistrar.class.getName());
        if(attributes != null) {
            String[] basePackages = (String[]) attributes.get("basePackages");
            if(basePackages.length == 0) {
                String clzName = importingClassMetadata.getClassName();
                basePackages = new String[]{ClassUtils.getPackageName(clzName)};
            }
            ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
            scanner.addIncludeFilter(new AnnotationTypeFilter(Component.class));
            scanner.addIncludeFilter(new AnnotationTypeFilter(RpcService.class));
            log.info("start scan RpcService and Component");
            scanner.scan(basePackages);
            log.info("scan end");
        }
    }
}
