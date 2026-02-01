package com.pu.controller;

import com.pu.epojo.Job;
import com.pu.epojo.JobQueryParam;
import com.pu.epojo.Result;
import com.pu.service.JobService;
import com.pu.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobServer;

    //用户一进来就该看见的岗位信息页面
    @GetMapping()
    public Result jobList(JobQueryParam jobQueryParam) {
        log.info("在主页面分页查询岗位");
        return Result.success(jobServer.jobList(jobQueryParam));
    }

    //查看单个岗位详情
    @GetMapping("/{id}")
    public Result job(Long id) {
        log.info("查询了{}的信息",id);
        return Result.success(jobServer.getJobById(id));
    }

    //公司新发布岗位
    @PostMapping
    public Result AddJob(@RequestBody Job job) {
        log.info("新增了一个{}工作岗位",job.getName());
        jobServer.addJob(job);
        return Result.success();
    }
    //批量删除
    @DeleteMapping
    public Result deleteJob(Long[] ids) {
        jobServer.deleteJob(ids);
        return Result.success();
    }

    @PutMapping
    public Result updateJob(@RequestBody Job job) {
        jobServer.updatejob(job);
        return Result.success();
    }
    //查看自己公司的岗位
}
