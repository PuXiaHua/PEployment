package com.pu.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class ApplyEventMessage {
    private Long applyId;
    private Long userId;
    private Long jobId;
    private String eventType; // CREATED / CANCELED / PASSED / REJECTED
    private String messageId;
    private LocalDateTime applyTime;
}
