package com.cloud.aws.sqs;

import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class SQSListener {

    @SqsListener(value = "my_queue", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void processMessages(String message) {
        System.out.println(message);
    }
}
