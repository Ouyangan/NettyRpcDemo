package com.hunt.client;

import com.hunt.protocol.RpcDecoder;
import com.hunt.protocol.RpcEncoder;
import com.hunt.protocol.RpcRequest;
import com.hunt.protocol.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    private static final Logger log = LoggerFactory.getLogger(RpcClientHandler.class);
    private final String host;
    private final int port;

    private RpcResponse response;

    private  EventLoopGroup group = new NioEventLoopGroup();
    private  Bootstrap bootstrap = new Bootstrap();

    public RpcClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("api caught exception", cause);
        ctx.close();
    }

    public RpcResponse send(RpcRequest request) throws InterruptedException {
        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new RpcEncoder(RpcRequest.class))
                                    .addLast(new RpcDecoder(RpcResponse.class))
                                    .addLast(RpcClientHandler.this);
                        }
                    })
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .connect(host, port)
                    .sync()
                    .channel()
                    .writeAndFlush(request)
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();
            return response;
        } finally {
            group.shutdownGracefully();
        }
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, RpcResponse response) throws Exception {
        this.response = response;
    }
}
