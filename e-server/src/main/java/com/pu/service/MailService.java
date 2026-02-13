package com.pu.service;

public interface MailService {
    void send(String to, String subject, String content);
}
