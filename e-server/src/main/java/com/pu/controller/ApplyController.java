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

    @PostMapping
    public Result addApply(@RequestBody Application application) {
        applyService.addApply(application);
        return Result.success();
    }


    @PutMapping("/{id}")
    public Result cancelApply(@PathVariable Long id) {
        applyService.cancelApply(id);
        return Result.success();
    }

    @GetMapping
    public Result getApply() {
        return Result.success(applyService.getApplyList());
    }
}
