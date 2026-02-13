package com.pu.service;

import com.pu.es.JobES;
import com.pu.es.JobESRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobESService {

    private final JobESRepository jobESRepository;

    public List<JobES> searchJobs(String keyword) {
        return jobESRepository.findByTitleContainingOrDescriptionContainingOrTagsContaining(keyword, keyword, keyword);
    }

    public void saveJob(JobES jobES) {
        jobESRepository.save(jobES); // 新增或更新ES文档
    }
}
