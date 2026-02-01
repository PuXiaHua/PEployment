package com.pu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pu.epojo.Job;
import com.pu.epojo.JobQueryParam;
import com.pu.epojo.PageResult;
import com.pu.epojo.User;
import com.pu.mapper.JobMapper;
import com.pu.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobMapper jobMapper;

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
        //todo 拦截器threadlocal存用户身份 校验用户权限 公司权限才允许发布岗位
        //if ()
        job.setCreateTime(LocalDateTime.now());
        job.setUpdateTime(LocalDateTime.now());
        jobMapper.addJob(job);
    }

    @Override
    public void deleteJob(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return;
        }
        //todo 拦截器threadlocal存用户身份 校验用户权限 公司权限才允许发布岗位
        jobMapper.deleteJobById(ids);
    }

    @Override
    public void updatejob(Job job) {
        //todo 拦截器threadlocal存用户身份 校验用户权限 公司权限才允许发布岗位
        job.setUpdateTime(LocalDateTime.now());
        jobMapper.updateJob(job);
    }
}
