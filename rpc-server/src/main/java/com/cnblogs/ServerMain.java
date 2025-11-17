package com.cnblogs;

import com.cnblogs.annotations.RpcScan;
import com.cnblogs.nettry.server.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
@RpcScan(basePackages = "com.cnblogs")
public class ServerMain {
    public static void main(String[] args) {
        autoRegistry();
    }

    public static void autoRegistry() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ServerMain.class);
        NettyServer nettyServer = (NettyServer) annotationConfigApplicationContext.getBean(NettyServer.class);
        // 启动服务端
        nettyServer.start();
    }
}
