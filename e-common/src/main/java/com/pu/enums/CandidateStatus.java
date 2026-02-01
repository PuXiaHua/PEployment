package com.pu.common.enums;

import lombok.Getter;

public enum CandidateStatus {
    INIT(0, "已投递"),
    RESUME_PASS(1, "简历通过"),
    INTERVIEW(2, "面试中"),
    OFFER(3, "已发 Offer"),
    REJECT(4, "已淘汰");
    @Getter
    private Integer code;
    private String desc;

    CandidateStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
