package github.javaguide;

import github.javaguide.annotations.RpcService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RpcService(group = "test1", version = "version1")
public class HelloServiceImpl implements HelloService {

    static {
        System.out.println("HelloService被创建");
    }

    @Override
    public String hello(Hello hello) {
        log.info("Server: HelloService收到msg: {}.", hello.getMsg());
        String description = "Hello description is " + hello.getDescription();
        log.info("Server: HelloService回复: {}", description);
        return description;
    }
}
