package com.washserver.web.protocol.model;

public enum MsgResCodeEnum {
    /**
     * 成功
     */
    SUCCESS((byte)0),
    /**
     * 失败
     */
    FAIL((byte)1)
    ;
    private byte code;

    MsgResCodeEnum(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
