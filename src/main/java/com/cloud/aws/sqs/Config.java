package com.cloud.aws.sqs;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    private final AWSCredentialsProvider profileCredentialsProvider;

    public Config(AWSCredentialsProvider credentialsProvider) {
        this.profileCredentialsProvider = credentialsProvider;
    }


    @Bean
    public QueueMessagingTemplate queueMessagingTemplate() {
        AmazonSQSAsync sqsClient = AmazonSQSAsyncClientBuilder.standard().withCredentials(profileCredentialsProvider).build();
        return new QueueMessagingTemplate(sqsClient);
    }
}
