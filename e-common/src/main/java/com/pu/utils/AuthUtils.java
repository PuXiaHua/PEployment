package com.pu.utils;

import com.pu.enums.UserRole;
import com.pu.context.UserContext;
import com.pu.epojo.User;
import com.pu.exception.BizException;

public class AuthUtils {
    public static void requireCompany(){
        User u = UserContext.getUser();
        if (u == null || u.getRole() != UserRole.COMPANY.getCode()) {
            throw new BizException("无权限");
        }
    }

    public static void requireJobSeeker() {
        User u = UserContext.getUser();
        if (u == null || u.getRole() != UserRole.EMPLOYEE.getCode()) {
            throw new BizException("无权限");
        }
    }
}
