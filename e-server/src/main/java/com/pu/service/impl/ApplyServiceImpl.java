package com.pu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pu.context.UserContext;
import com.pu.edto.ApplicationDTO;
import com.pu.enums.CandidateStatus;
import com.pu.epojo.Application;
import com.pu.epojo.PageResult;
import com.pu.exception.BizException;
import com.pu.mapper.ApplyMapper;
import com.pu.service.ApplyService;
import com.pu.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ApplyServiceImpl implements ApplyService {

    private final ApplyMapper applyMapper;

    @Override
    public void addApply(Application application) {
        AuthUtils.requireJobSeeker();
        Long userId = UserContext.getUser().getId();
        application.setUserId(userId);
        Application exist = applyMapper.getApplyByUserAndJob(userId, application.getJobId());
        if (exist == null) {
            application.setCreateTime(LocalDateTime.now());
            application.setUpdateTime(LocalDateTime.now());
            application.setStatus(CandidateStatus.INIT.getCode());
            applyMapper.addApply(application);
            return;
        }
        if (CandidateStatus.CANCEL.getCode().equals(exist.getStatus())) {
            exist.setUpdateTime(LocalDateTime.now());
            exist.setStatus(CandidateStatus.EXHAUST.getCode());
            exist.setResumeSnapshot(application.getResumeSnapshot());
            applyMapper.updateStatus(exist);
            return;
        }
        throw new BizException("该岗位已投递，无法重复投递");
    }

    public void cancelApply(Long Id) {
        AuthUtils.requireJobSeeker();
        Application exist = applyMapper.getApplyById(Id);
        if (exist == null) {
            throw new BizException("未投递该岗位，无法撤销");
        }
        if (exist.getStatus() != CandidateStatus.INIT.getCode()) {
            throw new BizException("当前状态不可撤销");
        }
        exist.setStatus(CandidateStatus.CANCEL.getCode());
        exist.setUpdateTime(LocalDateTime.now());
        applyMapper.updateStatus(exist);
    }

    @Override
    public PageResult<ApplicationDTO> getApplyList() {
        PageHelper.startPage(1, 10);
        List<ApplicationDTO> applicationDTOList = applyMapper.getApplyByUserId(UserContext.getUser().getId());
        Page page=(Page) applicationDTOList;
        return new PageResult<>(page.getTotal(),page.getResult());
    }
}
