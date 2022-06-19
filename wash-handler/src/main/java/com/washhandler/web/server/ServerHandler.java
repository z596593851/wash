package com.washhandler.web.server;


import com.washhandler.web.protocol.model.Message;
import com.washhandler.web.protocol.model.MsgTypeEnum;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerHandler extends SimpleChannelInboundHandler<Message> {
    private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        log.info("服务端收到请求:"+msg.toString());
        byte type=msg.getType();
        Message res=new Message();
        res.setId(msg.getId());
        res.setContent("收到了");
        if(type== MsgTypeEnum.REQUEST.getType()){
            res.setType(MsgTypeEnum.RESPONSE.getType());
        }else if(type==MsgTypeEnum.SYN_REQUEST.getType()){
            res.setType(MsgTypeEnum.SYN_RESPONSE.getType());
            //模拟处理耗时
            Thread.sleep(3000);
        }
        log.info("服务端发送回应:{}",res);
        ctx.writeAndFlush(res);
    }
}
