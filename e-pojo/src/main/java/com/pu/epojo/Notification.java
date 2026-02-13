package com.pu.epojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Notification {

    private Long id;
    //接收用户ID
    private Long userId;
    //通知标题
    private String title;
    //通知内容
    private String content;
    //业务类型 APPLY_PASSED / APPLY_REJECTED
    private String bizType;
    //是否已读 0未读 1已读
    private Integer isRead;
    //通知渠道 SYSTEM / SMS / EMAIL
    private String channel;
    //发送状态 0待发送 1成功 2失败
    private Integer sendStatus;
    //重试次数
    private Integer retryCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
