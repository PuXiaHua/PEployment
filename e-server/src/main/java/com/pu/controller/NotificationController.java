package com.pu.controller;

import com.pu.epojo.Result;
import com.pu.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notify")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/{id}")
    public Result getNotification(@PathVariable Long id) {
        return Result.success(notificationService.getById(id));
    }

    @GetMapping
    public Result getAllNotification(){
        return Result.success(notificationService.getNotifyList());
    }
}
