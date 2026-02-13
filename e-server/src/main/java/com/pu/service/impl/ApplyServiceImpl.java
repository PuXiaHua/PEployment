package com.pu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pu.context.UserContext;
import com.pu.edto.ApplicationDTO;
import com.pu.enums.CandidateStatus;
import com.pu.epojo.Application;
import com.pu.epojo.Company;
import com.pu.epojo.Job;
import com.pu.epojo.PageResult;
import com.pu.exception.BizException;
import com.pu.mapper.ApplyMapper;
import com.pu.message.*;
import com.pu.mq.producer.ApplyProducer;
import com.pu.service.ApplyService;
import com.pu.service.CompanyService;
import com.pu.service.JobService;
import com.pu.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ApplyServiceImpl implements ApplyService {

    private final ApplyMapper applyMapper;
    private final CompanyService companyService;
    private final JobService jobService;
    private final ApplyProducer applyProducer;

    @Override
    public void addApply(Application application) {
        AuthUtils.requireJobSeeker();
        Long userId = UserContext.getUser().getId();
        Long jobId = application.getJobId();
        application.setUserId(userId);
        Application exist = applyMapper.getApplyByUserAndJob(userId, jobId);
        // 首次投递
        if (exist == null) {
            application.setCreateTime(LocalDateTime.now());
            application.setUpdateTime(LocalDateTime.now());
            application.setCancelCount(0);
            application.setStatus(CandidateStatus.INIT.getCode());
            applyMapper.addApply(application);
            applyProducer.sendEvent(new ApplyEventMessage(application.getId(), userId, jobId, "CREATED", null, LocalDateTime.now()));
            return;
        }
        // 撤销后再次投递 第二次机会 回到INIT
        if (exist.getStatus() == CandidateStatus.CANCEL.getCode()) {
            exist.setUpdateTime(LocalDateTime.now());
            exist.setStatus(CandidateStatus.INIT.getCode());
            exist.setResumeSnapshot(application.getResumeSnapshot());
            applyMapper.updateStatus(exist);
            applyProducer.sendEvent(new ApplyEventMessage(application.getId(), userId, jobId, "CREATED", null, LocalDateTime.now()));
            return;
        }
        throw new BizException("该岗位已投递，无法重复投递");
    }

    public void cancelApply(Long id) {
        AuthUtils.requireJobSeeker();
        Application exist = applyMapper.getById(id);
        if (exist == null) {
            throw new BizException("未投递该岗位，无法撤销");
        }
        if (exist.getStatus() != CandidateStatus.INIT.getCode()) {
            throw new BizException("当前状态不可撤销");
        }
        // 已经撤销过一次 INIT -> CANCEL -> INIT -> CANCEL -> EXHAUST
        if (exist.getCancelCount() >= 1) {
            exist.setStatus(CandidateStatus.EXHAUST.getCode());
        } else {
            exist.setStatus(CandidateStatus.CANCEL.getCode());
            exist.setCancelCount(exist.getCancelCount() + 1);
        }
        exist.setUpdateTime(LocalDateTime.now());
        applyMapper.updateStatus(exist);
        applyProducer.sendEvent(new ApplyEventMessage(exist.getId(), UserContext.getUser().getId(), exist.getJobId(), "CANCELED", null, LocalDateTime.now()));
    }

    @Override
    public PageResult<ApplicationDTO> getApplyList() {
        PageHelper.startPage(1, 10);
        List<ApplicationDTO> applicationDTOList = applyMapper.getApplyByUserId(UserContext.getUser().getId());
        Page page = (Page) applicationDTOList;
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Override
    public PageResult<ApplicationDTO> getCompanyApply() {
        AuthUtils.requireCompany();
        Company companyDB = companyService.getCompanyInfoByUserId();
        if (companyDB == null) {
            throw new BizException("当前企业尚未创建公司");
        }
        PageHelper.startPage(1, 10);
        List<ApplicationDTO> applicationDTOList = applyMapper.getApplyByCompanyId(companyDB.getId());
        Page page = (Page) applicationDTOList;
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Override
    public void rejectApply(Long id) {
        Application app = applyMapper.getById(id);
        Long userId = app.getUserId();
        Long jobId = app.getJobId();
        if (app == null) {
            throw new BizException("申请不存在");
        }
        if (!CandidateStatus.INIT.getCode().equals(app.getStatus())) {
            throw new BizException("该申请已被处理，无法重复操作");
        }
        //注意权限校验 是不是公司 是不是当前公司的岗位
        AuthUtils.requireCompany();
        Company company = companyService.getCompanyInfoByUserId();
        Job job = jobService.getJobById(app.getJobId());
        if (!job.getCompanyId().equals(company.getId())) {
            throw new BizException("无权处理该岗位的申请");
        }
        applyMapper.rejectApply(id);
        applyProducer.sendEvent(new ApplyEventMessage(id, userId, jobId, "REJECTED", null, LocalDateTime.now()));
        applyProducer.sendNotification(new ApplyEventMessage(id, userId, jobId, "REJECTED", null, LocalDateTime.now()));
    }

    @Override
    public void passApply(Long id) {
        Application app = applyMapper.getById(id);
        Long userId = app.getUserId();
        Long jobId = app.getJobId();
        if (app == null) {
            throw new BizException("申请不存在");
        }
        if (!CandidateStatus.INIT.getCode().equals(app.getStatus())) {
            throw new BizException("该申请已被处理，无法重复操作");
        }
        AuthUtils.requireCompany();
        Company company = companyService.getCompanyInfoByUserId();
        Job job = jobService.getJobById(app.getJobId());
        if (!job.getCompanyId().equals(company.getId())) {
            throw new BizException("无权处理该岗位的申请");
        }
        AuthUtils.requireCompany();
        applyMapper.passApply(id);
        applyProducer.sendEvent(new ApplyEventMessage(id, userId, jobId, "PASSED", null, LocalDateTime.now()));
        applyProducer.sendNotification(new ApplyEventMessage(id, userId, jobId, "PASSED", null, LocalDateTime.now()));
    }
}
