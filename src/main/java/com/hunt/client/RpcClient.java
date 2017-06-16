package com.hunt.client;

import com.hunt.protocol.RpcRequest;
import com.hunt.protocol.RpcResponse;
import com.hunt.registry.ServiceDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Proxy;
import java.util.UUID;

public class RpcClient {
    private static final Logger log = LoggerFactory.getLogger(RpcClient.class);
    private String serviceAddress;
    private ServiceDiscovery serviceDiscovery;

    public RpcClient(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public RpcClient(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public <T> T create(final Class<T> cls) {
        return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class<?>[]{cls}, (proxy, method, args) -> {
            RpcRequest request = new RpcRequest();
            request.setRequestId(UUID.randomUUID().toString());
            request.setClassName(method.getDeclaringClass().getName());
            request.setMethodName(method.getName());
            request.setParameters(args);
            request.setParameterTypes(method.getParameterTypes());
            String discover = serviceDiscovery.discover(cls.getName());
            String[] split = discover.split(":");
            String host = split[0];
            int port = Integer.parseInt(split[1]);

            RpcClientHandler handler = new RpcClientHandler(host, port);
            RpcResponse send = handler.send(request);
            if (send == null) {
                throw new RuntimeException("response is null");
            }
            if (StringUtils.hasText(send.getError())) {
                return send.getError();
            }
            return send.getResult();
        });
    }
}
