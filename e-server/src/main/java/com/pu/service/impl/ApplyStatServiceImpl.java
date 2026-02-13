package com.pu.service.impl;

import com.pu.mapper.ApplyStatMapper;
import com.pu.message.ApplyEventMessage;
import com.pu.service.ApplyStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplyStatServiceImpl implements ApplyStatService {

    private final ApplyStatMapper applyStatMapper;

    @Transactional(rollbackFor = Exception.class)
    public void handleApplyCreated(ApplyEventMessage message) {
        Long jobId = message.getJobId();
        try {
            applyStatMapper.addNewMessage(message.getMessageId());
        } catch (DuplicateKeyException e) {
            // 说明已经处理过
            return;
        }
        int rows = applyStatMapper.increaseApplyCount(jobId);
        if (rows == 0) {
            applyStatMapper.insertIfNotExist(jobId);
            applyStatMapper.increaseApplyCount(jobId);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleApplyCanceled(ApplyEventMessage message) {
        try {
            applyStatMapper.addNewMessage(message.getMessageId());
        } catch (DuplicateKeyException e) {
            return;
        }
        applyStatMapper.increaseCancelCount(message.getJobId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleApplyPassed(ApplyEventMessage message) {
        try {
            applyStatMapper.addNewMessage(message.getMessageId());
        } catch (DuplicateKeyException e) {
            return;
        }
        applyStatMapper.increasePassCount(message.getJobId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleApplyRejected(ApplyEventMessage message) {
        try {
            applyStatMapper.addNewMessage(message.getMessageId());
        } catch (DuplicateKeyException e) {
            return;
        }
        applyStatMapper.increaseRejectCount(message.getJobId());
    }
}

