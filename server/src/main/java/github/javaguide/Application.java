package github.javaguide;

import github.javaguide.annotations.RpcScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@RpcScan(basePackages = {"github.javaguide"})
public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(Application.class);

    }
}
