package com.washserver.web.client;


import com.washserver.web.protocol.model.Message;
import com.washserver.web.protocol.model.MsgTypeEnum;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler extends SimpleChannelInboundHandler<Message> {
    private Client client;
    public ClientHandler(Client client){
        super();
        this.client=client;
    }

    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        log.info("客户端收到回应:{}",msg);
        byte type=msg.getType();
        if(type==MsgTypeEnum.SYN_RESPONSE.getType()){
            client.ackSyncMsg(msg);
        }
    }
}
