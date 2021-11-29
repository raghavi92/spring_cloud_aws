package com.cloud.aws.sqs;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SQSSender {

    private QueueMessagingTemplate queueMessagingTemplate;
    private AmazonSQSAsync sqsClient;
    private static final String SPAN_CTX = "span_ctx";

    public SQSSender(QueueMessagingTemplate queueMessagingTemplate, AmazonSQSAsync sqsClient) {
        this.queueMessagingTemplate = queueMessagingTemplate;
        this.sqsClient = sqsClient;
    }

    public void sendMessage(String message) {
        queueMessagingTemplate.send("test-queue-opentracing",
                MessageBuilder.withPayload(message)
                .build()
        );
    }

    public void sendMessageWithTracingAttribute(String message) {
        String messageJson = "serialized span context";

        MessageAttributeValue messageValue = new MessageAttributeValue()
                .withDataType("String")
                .withStringValue(messageJson);

        SendMessageRequest request = new SendMessageRequest()
                .withQueueUrl("test-queue-opentracing")
                .withMessageAttributes(Map.of(SPAN_CTX, messageValue))
                .withMessageBody(message);

        sqsClient.sendMessage(request);
    }
}
