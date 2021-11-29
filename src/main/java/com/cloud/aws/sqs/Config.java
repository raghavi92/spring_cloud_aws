package com.cloud.aws.sqs;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.cloud.config.TracingContextResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import static java.util.List.of;

@Configuration
public class Config {

    private final AWSCredentialsProvider profileCredentialsProvider;

    public Config(AWSCredentialsProvider credentialsProvider) {
        this.profileCredentialsProvider = credentialsProvider;
    }

    @Bean
    @Primary
    public AmazonSQSAsync sqsClient() {
        return AmazonSQSAsyncClientBuilder.standard().withCredentials(profileCredentialsProvider).build();
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync sqsClient) {
        return new QueueMessagingTemplate(sqsClient);
    }

    @Bean
    public QueueMessageHandlerFactory queueMessageHandlerFactory(AmazonSQSAsync amazonSQSAsync,
                                                                 ObjectMapper objectMapper,
                                                                 TracingContextResolver tracingContextResolver) {
        QueueMessageHandlerFactory queueMessageHandlerFactory = new QueueMessageHandlerFactory();
        queueMessageHandlerFactory.setAmazonSqs(amazonSQSAsync);
        MappingJackson2MessageConverter mappingJackson2MessageConverter = new MappingJackson2MessageConverter();
        mappingJackson2MessageConverter.setObjectMapper(objectMapper);
        queueMessageHandlerFactory.setMessageConverters(of(mappingJackson2MessageConverter));
        queueMessageHandlerFactory.setArgumentResolvers(of(tracingContextResolver));
        return queueMessageHandlerFactory;
    }

}
