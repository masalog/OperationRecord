package com.example.OperationRecord.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linecorp.bot.client.LineMessagingClient;


@Configuration
@ConditionalOnProperty(name = "aws.sqs.enabled", havingValue = "true")
public class LineBotConfig {

    @Value("${LINE_CHANNEL_ACCESS_TOKEN}")
    private String token;

    @Bean
    public LineMessagingClient lineMessagingClient() {
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("LINE_CHANNEL_ACCESS_TOKEN is not set");
        }
        return LineMessagingClient.builder(token).build();
    }
}


