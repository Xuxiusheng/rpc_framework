package github.javaguide.remoting.transport.netty.server;

import github.javaguide.config.CustomShutDownHook;
import github.javaguide.remoting.transport.netty.codec.RpcMessageCodec;
import github.javaguide.remoting.transport.netty.codec.RpcMessageFrameDecoder;
import github.javaguide.utils.RuntimeUtil;
import github.javaguide.utils.concurrent.threadpool.ThreadPoolFactoryUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class NettyServer {
    public static final int PORT = 9998;

    @SneakyThrows
    public void start() {
        // start之前先清理资源，避免重复注册冲突，清除残留状态
        CustomShutDownHook.getInstance().clear();

        // 获取本机IP地址
        String host = InetAddress.getLocalHost().getHostAddress();

        // 接收客户端连接
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 处理网络I/O
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        // 处理业务逻辑
        DefaultEventExecutorGroup serviceHandlerGroup = new DefaultEventExecutorGroup(
                RuntimeUtil.cpus() * 2,
                ThreadPoolFactoryUtil.createThreadFactory("service-handler-group", false)
        );

        try {
            ServerBootstrap b = new ServerBootstrap();
            RpcMessageCodec rpcMessageCodec = new RpcMessageCodec();
            NettyServerHandler nettyServerHandler = new NettyServerHandler();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            // 30秒内没有数据可读，关闭连接
                            p.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            // 接收到消息进行解码
                            p.addLast(new RpcMessageFrameDecoder());
                            // RPCMessage       编解码器
                            p.addLast(rpcMessageCodec);
                            // 可共享的 serverHandler
                            p.addLast(serviceHandlerGroup, nettyServerHandler);
                        }
                    });

            ChannelFuture f = b.bind(host, PORT).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("occur exception when start server:", e);
        } finally {
            log.error("shutdown bossGroup and workerGroup");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            serviceHandlerGroup.shutdownGracefully();
        }
    }
}