package com.pu.service;

import com.pu.epojo.User;
import org.springframework.stereotype.Service;


public interface UserService {

    User login(User user);

    void registy(User user);

    User getUserById(Long id);
}
