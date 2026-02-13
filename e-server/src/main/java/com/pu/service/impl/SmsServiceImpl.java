package com.pu.service.impl;

import com.pu.service.SmsService;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    @Override
    public void send(String phone, String content) {
        // 模拟发送
        System.out.println("模拟发送短信给: " + phone);
    }
}

