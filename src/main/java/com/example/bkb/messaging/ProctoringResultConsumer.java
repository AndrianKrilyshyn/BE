package com.example.bkb.messaging;

import com.example.bkb.dto.ProctoringResultMessage;
import com.example.bkb.service.ProctoringReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProctoringResultConsumer {

    private final ProctoringReportService reportService;

    @RabbitListener(queues = "proctoring.results")
    public void consume(ProctoringResultMessage msg) {
        log.info("Received proctoring result for attempt: {}", msg.attemptId());
        reportService.save(msg);
        log.info("Saved proctoring report for attempt: {}", msg.attemptId());
    }
}
