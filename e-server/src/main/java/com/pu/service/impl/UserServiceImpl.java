package com.pu.service.impl;

import com.pu.common.enums.AccountStatus;
import com.pu.common.enums.UserRole;
import com.pu.epojo.User;
import com.pu.mapper.UserMapper;
import com.pu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public User login(User user) {
        String username = user.getEmail() == null ? user.getPhone() : user.getEmail();
        User userFromDb = userMapper.getUser(username);
        //密码加密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (userFromDb != null && encoder.matches(user.getPassword(), userFromDb.getPassword())) {
            return userFromDb;
        }
        return null;
    }

    @Override
    public void registy(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setStatus(AccountStatus.NORMAL.getCode());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.registy(user);
    }
}
