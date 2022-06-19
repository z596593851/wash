package com.washhandler.web.protocol.model;

/**
 * 命令号枚举
 */
public enum MsgTypeEnum {
    /**
     * 心跳
     */
    HEARTBEATS((byte)0),
    /**
     * 客户端请求普通请求，不要求应答
     */
    REQUEST((byte)1),
    /**
     * 客户端同步请求，要求有应答
     */
    SYN_REQUEST((byte)2),
    /**
     * 服务端普通响应
     */
    RESPONSE((byte)3),
    /**
     * 服务端对于同步请求的应答
     */
    SYN_RESPONSE((byte)4)
    ;
    private byte type;

    MsgTypeEnum(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }



}
