package com.pu.mq.consumer;

import com.pu.message.*;
import com.pu.mq.constants.ApplyMQConstants;
import com.pu.service.ApplyStatService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplyStatConsumer {

    private final ApplyStatService applyStatService;

    @RabbitListener(queues = ApplyMQConstants.APPLY_STAT_QUEUE)
    public void onApplyEvent(
            ApplyEventMessage message,
            Channel channel,
            Message amqpMessage
    ) throws IOException {

        try {
            log.info("收到事件：{}", message);

            switch (message.getEventType()) {
                case "CREATED":
                    applyStatService.handleApplyCreated(message);
                    break;
                case "CANCELED":
                    applyStatService.handleApplyCanceled(message);
                    break;
                case "PASSED":
                    applyStatService.handleApplyPassed(message);
                    break;
                case "REJECTED":
                    applyStatService.handleApplyRejected(message);
                    break;
            }

            channel.basicAck(
                    amqpMessage.getMessageProperties().getDeliveryTag(),
                    false
            );

        } catch (Exception e) {

            channel.basicNack(
                    amqpMessage.getMessageProperties().getDeliveryTag(),
                    false,
                    false
            );
        }
    }
}
