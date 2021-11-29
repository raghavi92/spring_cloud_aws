package com.cloud;

import co.elastic.apm.attach.ElasticApmAttacher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AwsApplication {

    public static void main(String[] args) {
        ElasticApmAttacher.attach();
        SpringApplication.run(AwsApplication.class, args);
    }

}
