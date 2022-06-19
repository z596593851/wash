package com.washserver.web.protocol.model;

public enum MsgCmdEnum {
    /**
     * 缺省
     */
    DEFAULT((byte)0),
    /**
     * 新建任务
     */
    TASK((byte)1),
    /**
     * 开始任务
     */
    START((byte)2),
    /**
     * 暂停任务
     */
    STOP((byte)3),
    /**
     * 取消任务
     */
    CANCLE((byte)4)
    ;
    private byte cmd;

    MsgCmdEnum(byte cmd) {
        this.cmd = cmd;
    }

    public byte getCmd() {
        return cmd;
    }
}
