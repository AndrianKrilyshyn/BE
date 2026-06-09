package com.example.bkb.messaging;

import com.example.bkb.dto.ReportData;
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
    public void consume(ReportData data) {
        log.info("Received proctoring result for attempt: {}", data.attemptId());
        reportService.save(data);
        log.info("Saved proctoring report for attempt: {}", data.attemptId());
    }
}
