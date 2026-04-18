package com.example.OperationRecord.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linecorp.bot.client.LineMessagingClient;

@Configuration
public class LineBotConfig {

    @Value("${line.bot.channel-token}")
    private String channelToken;

    @Bean
    LineMessagingClient lineMessagingClient() {
        return LineMessagingClient.builder(channelToken).build();
    }
}

