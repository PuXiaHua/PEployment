package com.pu.service;

import com.pu.epojo.Notification;
import com.pu.epojo.PageResult;
import com.pu.message.ApplyEventMessage;

import java.util.List;

public interface NotificationService {

    void handleApplyResult(ApplyEventMessage message);

    PageResult<Notification> getNotifyList();

    void markAsRead(Long id, Long userId);

    Integer countUnread(Long userId);

    Notification getById(Long id);
}
