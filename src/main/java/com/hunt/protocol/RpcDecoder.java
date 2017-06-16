package com.hunt.protocol;

import com.hunt.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> cls;

    public RpcDecoder(Class<?> cls) {
        this.cls = cls;
    }

    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //数据长度
        if (in.readableBytes() < 4) {
            return;
        }
        //标记当前读取到的索引位置
        in.markReaderIndex();
        //获取自定义协议中的长度
        int dataLength = in.readInt();
        //如果当前数据的长度小于自定义协议中的长度则说明 tcp粘包了
        // 实际上tcp协议不是有专门处理tcp粘包问题的方案吗? 这里待确认
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        //构造字节数组容器
        byte[] data = new byte[dataLength];
        //读取相应大小的数据
        in.readBytes(data);
        //反序列话
        Object object = SerializationUtil.deserialize(data, cls);
        out.add(object);
        ctx.flush();
    }
}
