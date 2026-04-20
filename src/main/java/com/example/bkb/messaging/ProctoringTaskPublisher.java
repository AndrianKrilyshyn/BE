package com.example.bkb.messaging;

import com.example.bkb.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProctoringTaskPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.s3.bucket}")
    private String bucket;

    public void publish(UUID attemptId) {
        var payload = Map.of(
                "attemptId", attemptId.toString(),
                "s3BasePath", "s3://%s/attempts/%s/chunks/".formatted(bucket, attemptId)
        );
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.TASKS_QUEUE,
                payload
        );
    }
}
