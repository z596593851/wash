package com.washserver.web.client;

import com.washserver.web.KryoDecoder;
import com.washserver.web.KryoEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class ClientInit extends ChannelInitializer<SocketChannel> {
    private Client client;

    public ClientInit(Client client){
        super();
        this.client=client;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
//        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0,2,0, 2));
        ch.pipeline().addLast(new LengthFieldPrepender(2));
        ch.pipeline().addLast(new KryoDecoder());
        ch.pipeline().addLast(new KryoEncoder());
        /*连接读空闲检测*/
        ch.pipeline().addLast(new ReadTimeoutHandler(15));
        ch.pipeline().addLast(new ClientHandler(this.client));

    }
}
