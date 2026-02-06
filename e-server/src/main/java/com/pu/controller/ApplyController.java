package com.pu.controller;

import com.pu.epojo.Application;
import com.pu.epojo.Result;
import com.pu.service.ApplyService;
import io.lettuce.core.search.arguments.AggregateArgs;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor
public class ApplyController {

    private final ApplyService applyService;

    //投递申请
    @PostMapping
    public Result addApply(@RequestBody Application application) {
        log.info("{}投递申请{}...", application.getId(), application.getJobId());
        applyService.addApply(application);
        return Result.success();
    }

    //撤销申请
    @PutMapping("/{id}")
    public Result cancelApply(@PathVariable Long id) {
        log.info("{}撤销申请...", id);
        applyService.cancelApply(id);
        return Result.success();
    }

    //查询申请记录
    @GetMapping
    public Result getApply() {
        log.info("查看申请记录...");
        return Result.success(applyService.getApplyList());
    }

    @GetMapping("/company")
    public Result getCompanyApply() {
        log.info("查看公司收到的申请");
        return Result.success(applyService.getCompanyApply());
    }

    @PutMapping("company/{id}/pass")
    public Result passApply(@PathVariable Long id) {
        log.info("通过申请");
        applyService.passApply(id);
        return Result.success();
    }

    @PutMapping("company/{id}/reject")
    public Result rejectApply(@PathVariable Long id) {
        log.info("拒绝申请");
        applyService.rejectApply(id);
        return Result.success();
    }
}
