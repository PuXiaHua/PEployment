package com.pu.enums;

import lombok.Getter;

public enum CandidateStatus {
    INIT(1, "投递中"),
    REJECT(2, "已淘汰"),
    PASS(3, "通过"),
    CANCEL(4, "撤销"),
    EXHAUST(5, "重投次数耗尽");

    @Getter
    private Integer code;
    private String desc;

    CandidateStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
