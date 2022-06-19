package com.washserver.web.client;


import com.washserver.web.protocol.model.Message;
import com.washserver.web.protocol.model.MsgCmdEnum;
import com.washserver.web.protocol.model.MsgTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientService.class);

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.start(1096,"localhost");
        Message message=new Message(System.currentTimeMillis(), MsgTypeEnum.SYN_REQUEST.getType(), MsgCmdEnum.START.getCmd(), "测试信息");
        log.info("客户端发送信息:{}",message);
        Message rep = client.sendSync(message);
        log.info("回应是:{}",rep);
        log.info("处理接下来的逻辑");
    }
}
