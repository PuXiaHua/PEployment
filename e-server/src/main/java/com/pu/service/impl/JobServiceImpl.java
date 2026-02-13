package com.pu.service.impl;

import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.pu.epojo.*;
import com.pu.exception.BizException;
import com.pu.mapper.JobMapper;
import com.pu.service.CompanyService;
import com.pu.service.JobService;
import com.pu.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobMapper jobMapper;
    private final CompanyService companyService;
    private final StringRedisTemplate stringRedisTemplate;
    //redis key TTL
    private static final String JOB_DETAIL_KEY_PREFIX = "job:detail:";
    private static final Random random = new Random();
    private static final long JOB_DETAIL_TTL = 30; // 分钟
    private final Cache<Long, Job> hotJobCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();
    private RedissonClient redissonClient;

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

    public Job getJobByIdWithCache(Long jobId) {

        // 1. 先查本地缓存（热点）
        Job hotJob = hotJobCache.getIfPresent(jobId);
        if (hotJob != null) {
            return hotJob;
        }
        // 2. 再查Redis
        String key = JOB_DETAIL_KEY_PREFIX + jobId;
        String cache = stringRedisTemplate.opsForValue().get(key);
        if (cache != null) {
            if ("null".equals(cache)) {
                return null;
            }
            return JSONUtil.toBean(cache, Job.class);
        }
        // 3. 查DB redisson锁重建缓存线程
        RLock lock = redissonClient.getLock(JOB_DETAIL_KEY_PREFIX + jobId);
        lock.lock();
        try {
            Job job = jobMapper.getJobById(jobId);
            if (job == null) {
                stringRedisTemplate.opsForValue().set(key, "null", 2, TimeUnit.MINUTES);
                return null;
            }
            //回写redis
            stringRedisTemplate.opsForValue()
                    .set(key, JSONUtil.toJsonStr(job), JOB_DETAIL_TTL + random.nextInt(5), TimeUnit.MINUTES);
            //回写本地
            hotJobCache.put(jobId, job);

            return job;
        } finally {
            lock.unlock();
        }
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
        List<String> keys = Arrays.stream(ids)
                .map(id -> "job:detail:" + id)
                .toList();
        stringRedisTemplate.delete(keys);

        for (Long id : ids) {
            hotJobCache.invalidate(id);
        }
    }

    @Override
    public void updateJob(Job job) {
        AuthUtils.requireCompany();
        job.setUpdateTime(LocalDateTime.now());
        jobMapper.updateJob(job);
        //删缓存（保证一致性）
        String key = "job:detail:" + job.getId();
        stringRedisTemplate.delete(key);
        hotJobCache.invalidate(job.getId());
    }

    @Override
    public PageResult<Job> getMyPublishJob() {
        AuthUtils.requireCompany();
        PageHelper.startPage(1, 10);
        Page<Job> page = (Page<Job>) jobMapper.getMyPublishJob(companyService.getCompanyInfoByUserId().getId());
        return new PageResult<>(page.getTotal(), page.getResult());
    }


}
