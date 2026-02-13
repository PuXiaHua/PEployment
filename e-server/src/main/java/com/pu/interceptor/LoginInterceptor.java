package com.pu.interceptor;

import com.pu.context.UserContext;
import com.pu.epojo.User;
import com.pu.exception.BizException;
import com.pu.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //todo redis+session有状态的登录校验 强制踢人下线
        String userIdStr = request.getHeader("userId");
        if (userIdStr == null) {
            throw new BizException("未登录");
        }
        Long userId = Long.parseLong(userIdStr);
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new BizException("用户不存在");
        }
        UserContext.setUser(user);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.removeUser();
    }
}
