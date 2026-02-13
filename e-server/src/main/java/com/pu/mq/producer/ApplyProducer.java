package com.pu.mq.producer;

import com.pu.message.*;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import com.pu.mq.constants.ApplyMQConstants;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplyProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendEvent(ApplyEventMessage message) {
        message.setMessageId(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(
                ApplyMQConstants.APPLY_EXCHANGE,
                message.getEventType(),
                message
        );
    }
    public void sendNotification(ApplyEventMessage message) {
        rabbitTemplate.convertAndSend(ApplyMQConstants.APPLY_EXCHANGE,
                ApplyMQConstants.APPLY_NOTIFY_KEY,
                message);
    }
}
