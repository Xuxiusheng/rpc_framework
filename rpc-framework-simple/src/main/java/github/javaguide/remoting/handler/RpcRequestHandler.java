package github.javaguide.remoting.handler;

import github.javaguide.factory.SingletonFactory;
import github.javaguide.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcRequestHandler {
    private final ServiceProvider serviceProvider;

    public RpcRequestHandler() {
        serviceProvider = null;
    }
}
