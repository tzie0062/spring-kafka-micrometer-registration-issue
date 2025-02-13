package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public KafkaEventListener kafkaEventListener() {
        return new KafkaEventListener();
    }

    @Bean
    public RetryTopicConfiguration retryTopicConfiguration(
            KafkaTemplate<String, String> template,
            @Value("${kafka.topic.in}") String topicToInclude,
            @Value("${spring.application.name}") String appName) {
        return RetryTopicConfigurationBuilder
                .newInstance()
                .fixedBackOff(5000L)
                .maxAttempts(3)
                .retryTopicSuffix("-" + appName + ".retry")
                .suffixTopicsWithIndexValues()
                .dltSuffix("-" + appName + ".dlq")
                .includeTopic(topicToInclude)
                .create(template);
    }

    @Slf4j
    public static class KafkaEventListener {

        @KafkaListener(id = "demo-listener",  topics = {"${kafka.topic.in}"}, groupId = "${spring.kafka.consumer.group-id}",
                idIsGroup = false)
        public void handleEvent(String msg) {
            log.info("received regular event: {}", msg);
        }
    }
}
