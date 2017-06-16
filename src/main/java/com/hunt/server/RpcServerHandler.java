package com.hunt.server;

import com.hunt.protocol.RpcRequest;
import com.hunt.protocol.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;


public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger log = LoggerFactory.getLogger(RpcServerHandler.class);

    private Map<String, Object> handlerMap = null;


    public RpcServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    private Object handle(RpcRequest request) throws InvocationTargetException {
        Object bean = handlerMap.get(request.getClassName());
        FastClass fastClass = FastClass.create(bean.getClass());
        Object invoke = fastClass.getMethod(request.getMethodName(), request.getParameterTypes()).invoke(bean, request.getParameters());
        return invoke;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, RpcRequest request) throws Exception {
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        try {
            Object result = handle(request);
            response.setResult(result);
        } catch (InvocationTargetException e) {
            response.setError(e.getMessage());
            log.error("handle request error:{}", e);
        }
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
