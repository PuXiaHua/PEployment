package com.pu.service.impl;

import com.pu.service.MailService;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    @Override
    public void send(String to, String subject, String content) {
        // 模拟发送
        System.out.println("模拟发送邮件给: " + to);
    }
}

