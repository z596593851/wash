package com.washserver.web;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;
import com.washserver.web.protocol.kryo.KryoSerializer;

/**
 * 反序列化器
 */
public class KryoDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out){
        Object obj = KryoSerializer.deserialize(in);
        out.add(obj);
    }
}
