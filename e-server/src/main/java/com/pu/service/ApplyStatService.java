package com.pu.service;

import com.pu.message.ApplyEventMessage;

public interface ApplyStatService {
    void handleApplyCreated(ApplyEventMessage message);
    void handleApplyCanceled(ApplyEventMessage message);
    void handleApplyPassed(ApplyEventMessage message);
    void handleApplyRejected(ApplyEventMessage message);
}
