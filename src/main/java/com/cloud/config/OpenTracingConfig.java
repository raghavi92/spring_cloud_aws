package com.cloud.config;

import co.elastic.apm.opentracing.ElasticApmTracer;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentracing.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenTracingConfig {
    @Bean
    public Tracer getTracer() {
        return new ElasticApmTracer();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
