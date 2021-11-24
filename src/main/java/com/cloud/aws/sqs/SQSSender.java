package com.cloud.aws.sqs;

import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class SQSSender {

    private QueueMessagingTemplate queueMessagingTemplate;

    public SQSSender(QueueMessagingTemplate queueMessagingTemplate) {
        this.queueMessagingTemplate = queueMessagingTemplate;
    }

    public void sendMessage(String message) {
        queueMessagingTemplate.send("test-queue-opentracing",
                MessageBuilder.withPayload(message)
                .build()
        );
    }
}
