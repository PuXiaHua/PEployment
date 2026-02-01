package com.pu.common.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    EMPLOYEE(1, "求职者"),
    COMPANY(2, "公司");

    private Integer code;
    private String desc;

    UserRole(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static UserRole fromCode(Integer code) {
        for (UserRole role : values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("未知的用户角色 code: " + code);
    }
}
