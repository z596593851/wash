package com.washserver.web.protocol.model;

public class Message {
    /**
     * 消息id
     */
    private long id;
    /**
     * 消息类型
     */
    private byte type;
    /**
     * 命令号
     */
    private byte cmd;
    /**
     * 消息体
     */
    private Object content;

    public Message(){}

    public Message(long id, byte type, byte cmd, Object content) {
        this.id=id;
        this.type = type;
        this.cmd = cmd;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getCmd() {
        return cmd;
    }

    public void setCmd(byte cmd) {
        this.cmd = cmd;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id="+id+
                ", type=" + type +
                ", cmd=" + cmd +
                ", content=" + content.toString() +
                '}';
    }
}
