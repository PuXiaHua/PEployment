package com.pu.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public enum AccountStatus {
    NORMAL(1, "正常"), DISABLED(2, "禁用");
    private Integer code;
    private String desc;
}
