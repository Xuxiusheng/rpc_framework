package com.cnblogs.service.impl;

import com.cnblogs.HelloMessage;
import com.cnblogs.HelloService;
import com.cnblogs.annotations.RpcService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RpcService(version = "v.1", group = "g.1")
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(HelloMessage helloMessage) {
        log.info("HelloServiceImpl收到: [{}]", helloMessage.getMessage());
        String result = "Hello description is {}" + helloMessage.getDescription();
        log.info("HelloServiceImpl返回: [{}]", result);
        return result;
    }
}
