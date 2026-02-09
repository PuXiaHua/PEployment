package com.pu.service;

import com.pu.epojo.Job;
import com.pu.epojo.JobQueryParam;
import com.pu.epojo.PageResult;
import org.springframework.stereotype.Service;

public interface JobService {
    PageResult<Job> jobList(JobQueryParam jobQueryParam);

    Job getJobById(Long id);

    Job getJobByIdWithCache(Long id);

    void addJob(Job job);

    void deleteJob(Long[] ids);

    void updateJob(Job job);

    PageResult<Job> getMyPublishJob();
}
