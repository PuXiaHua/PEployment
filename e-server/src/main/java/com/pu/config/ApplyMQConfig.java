package com.pu.config;

import com.pu.mq.constants.ApplyMQConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplyMQConfig {
    @Bean
    public DirectExchange applyExchange() {
        return new DirectExchange(
                ApplyMQConstants.APPLY_EXCHANGE,
                true,   // durable
                false   // autoDelete
        );
    }

    @Bean
    public Queue applyStatQueue() {
        return QueueBuilder.durable(ApplyMQConstants.APPLY_STAT_QUEUE).build();
    }

    @Bean
    public Queue applyHotQueue() {
        return QueueBuilder.durable(ApplyMQConstants.APPLY_HOT_QUEUE).build();
    }

    @Bean
    public Queue applyNotifyQueue() {
        return QueueBuilder.durable(ApplyMQConstants.APPLY_NOTIFY_QUEUE).build();
    }

    @Bean
    public Binding applyStatBinding(Queue applyStatQueue,
                                    DirectExchange applyExchange) {
        return BindingBuilder
                .bind(applyStatQueue)
                .to(applyExchange)
                .with(ApplyMQConstants.APPLY_STAT_KEY);
    }

    @Bean
    public Binding applyHotBinding(Queue applyHotQueue,
                                   DirectExchange applyExchange) {
        return BindingBuilder
                .bind(applyHotQueue)
                .to(applyExchange)
                .with(ApplyMQConstants.APPLY_HOT_KEY);
    }

    @Bean
    public Binding applyNotifyBinding(Queue applyNotifyQueue,
                                      DirectExchange applyExchange) {
        return BindingBuilder
                .bind(applyNotifyQueue)
                .to(applyExchange)
                .with(ApplyMQConstants.APPLY_NOTIFY_KEY);
    }
}
