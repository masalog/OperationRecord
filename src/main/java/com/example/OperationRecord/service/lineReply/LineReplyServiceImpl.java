package com.example.OperationRecord.service.lineReply;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LineReplyServiceImpl implements LineReplyService {

    @Value("${line.channel.access.token}")
    private String channelAccessToken;

    @Value("${line.channel.api.reply-url}")
    private String replyUrl;

    private final RestTemplate restTemplate;

    public LineReplyServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void reply(String replyToken, Map<String, Object> message) {

        System.out.println("【LineReplyService】Reply API を呼び出します");
        System.out.println("replyToken = " + replyToken);
        System.out.println("message = " + message);

        Map<String, Object> body = Map.of(
            "replyToken", replyToken,
            "messages", List.of(message)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(channelAccessToken);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(replyUrl, request, String.class);

        System.out.println("【LineReplyService】LINE API Response = " + response.getStatusCode());
    }

}
