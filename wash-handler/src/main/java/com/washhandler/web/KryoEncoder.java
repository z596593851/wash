package com.washhandler.web;

import com.washserver.web.protocol.kryo.KryoSerializer;
import com.washserver.web.protocol.model.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


/**
 * 序列化器
 */
public class KryoEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message message,
                          ByteBuf out) throws Exception {
        KryoSerializer.serialize(message, out);
        ctx.flush();
    }
}
