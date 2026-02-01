package com.pu.controller;

import com.pu.epojo.Result;
import com.pu.epojo.User;
import com.pu.service.UserService;
import com.pu.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @PostMapping
    public Result login(@RequestBody User user) {
        User loginUser=userService.login(user);
        log.info("用户登录");
        if (loginUser != null) {
            return Result.success();
        }
        return Result.error("用户名或密码错误");
    }

    @PostMapping("/registy")
    public Result registy(@RequestBody User user) {
        userService.registy(user);
        log.info("用户身份 {} 完成注册",user.getRole());
        //todo 全局异常处理器返回当前用户已存在result.error
        return Result.success();
    }
}
