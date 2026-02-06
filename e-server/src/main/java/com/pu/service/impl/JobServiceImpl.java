package com.pu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.pu.context.UserContext;
import com.pu.epojo.*;
import com.pu.exception.BizException;
import com.pu.mapper.JobMapper;
import com.pu.service.CompanyService;
import com.pu.service.JobService;
import com.pu.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobMapper jobMapper;
    private final CompanyService companyService;

    @Override
    public PageResult<Job> jobList(JobQueryParam jobQueryParam) {
        PageHelper.startPage(jobQueryParam.getPage(), jobQueryParam.getRows());
        List<Job> jobList = jobMapper.findAll(jobQueryParam);
        Page<Job> jobPage = (Page<Job>) jobList;
        return new PageResult<>(jobPage.getTotal(), jobPage.getResult());
    }

    @Override
    public Job getJobById(Long id) {
        return jobMapper.getJobById(id);
    }

    @Override
    public void addJob(Job job) {
        AuthUtils.requireCompany();
        Company company = companyService.getCompanyInfoByUserId();
        if (company == null) {
            throw new BizException("请先创建公司信息");
        }
        job.setCompanyId(company.getId());
        job.setCreateTime(LocalDateTime.now());
        job.setUpdateTime(LocalDateTime.now());
        jobMapper.addJob(job);
    }

    @Override
    public void deleteJob(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return;
        }
        AuthUtils.requireCompany();
        jobMapper.deleteJobById(ids);
    }

    @Override
    public void updatejob(Job job) {
        AuthUtils.requireCompany();
        job.setUpdateTime(LocalDateTime.now());
        jobMapper.updateJob(job);
    }

    @Override
    public PageResult<Job> getMyPublishJob() {
        AuthUtils.requireCompany();
        PageHelper.startPage(1, 10);
        Page<Job> page = (Page<Job>) jobMapper.getMyPublishJob(companyService.getCompanyInfoByUserId().getId());
        return new PageResult<>(page.getTotal(), page.getResult());
    }
}
