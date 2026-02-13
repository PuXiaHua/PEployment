package com.pu.service.impl;

import com.pu.context.UserContext;
import com.pu.epojo.PageResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pu.epojo.Notification;
import com.pu.epojo.User;
import com.pu.exception.BizException;
import com.pu.mapper.NotificationMapper;
import com.pu.mapper.UserMapper;
import com.pu.message.ApplyEventMessage;
import com.pu.service.MailService;
import com.pu.service.NotificationService;
import com.pu.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationMapper notificationMapper;
    private final UserMapper userMapper;
    private final MailService mailService;
    private final SmsService smsService;

    @Override
    public void handleApplyResult(ApplyEventMessage message) {

        //查询用户
        User user = userMapper.getUserById(message.getUserId());
        if (user == null) {
            return;
        }

        //构造通知内容
        String title;
        String content;

        if ("PASSED".equals(message.getEventType())) {
            title = "投递结果通知";
            content = "恭喜您，您的投递已通过！";
        } else if ("REJECTED".equals(message.getEventType())) {
            title = "投递结果通知";
            content = "很遗憾，您的投递未通过。";
        } else {
            return;
        }

        //插入站内信
        Notification notification = new Notification();
        notification.setUserId(user.getId());
        notification.setTitle(title);
        notification.setContent(content);
        notification.setIsRead(0);
        notification.setSendStatus(0);
        notification.setCreateTime(LocalDateTime.now());
        notification.setBizType("APPLY");
        notification.setChannel("SYSTEM");
        notification.setRetryCount(0);

        notificationMapper.insert(notification);

        //发送外部通知（邮件/短信）
        boolean success = false;

        try {
            if (StringUtils.hasText(user.getEmail())) {
                mailService.send(user.getEmail(), title, content);
                success = true;
            } else if (StringUtils.hasText(user.getPhone())) {
                smsService.send(user.getPhone(), content);
                success = true;
            }
        } catch (Exception e) {
            success = false;
        }

        //更新发送状态
        notification.setSendStatus(success ? 1 : 2);
        notificationMapper.updateSendStatus(notification);
    }

    @Override
    public PageResult<Notification> getNotifyList() {
        PageHelper.startPage(1, 10);
        Page page = (Page) notificationMapper.getNotifyList(UserContext.getUser().getId());
        return new PageResult<Notification>(page.getTotal(),page.getResult());
    }

    @Override
    public void markAsRead(Long id, Long userId) {
        int rows = notificationMapper.markAsRead(id, userId);
        if (rows == 0) {
            throw new BizException("通知不存在或无权限");
        }
    }

    @Override
    public Integer countUnread(Long userId) {
        return notificationMapper.countUnread(userId);
    }

    @Override
    public Notification getById(Long id) {
        Long userId = UserContext.getUser().getId();
        Notification notification = notificationMapper.getById(id, userId);
        if (notification == null) {
            throw new BizException("通知不存在或无权限");
        }
        if (notification.getIsRead() == 0) {
            notificationMapper.markAsRead(id, userId);
            notification.setIsRead(1);
        }
        return notification;
    }

}

