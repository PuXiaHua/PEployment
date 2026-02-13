package com.pu.mq.consumer;

import com.pu.message.ApplyEventMessage;
import com.pu.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.pu.mq.constants.ApplyMQConstants.APPLY_NOTIFY_QUEUE;

@RequiredArgsConstructor
@Component
public class ApplyResultConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = APPLY_NOTIFY_QUEUE)
    public void onMessage(ApplyEventMessage message) {
        notificationService.handleApplyResult(message);
    }
}
