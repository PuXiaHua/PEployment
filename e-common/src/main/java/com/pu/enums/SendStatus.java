package com.pu.enums;

public enum SendStatus {

    INIT(0, "待发送"),
    SUCCESS(1, "发送成功"),
    FAIL(2, "发送失败");

    private final int code;
    private final String desc;

    SendStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static SendStatus fromCode(int code) {
        for (SendStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid send status code");
    }
}

