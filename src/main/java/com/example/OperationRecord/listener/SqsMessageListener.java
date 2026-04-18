package com.example.OperationRecord.listener;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.OperationRecord.dto.LineInputDto;
import com.example.OperationRecord.service.flow.OperationRecordFlowService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.PushMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsMessageListener {

    private final SqsClient sqsClient;
    private final ObjectMapper mapper;
    private final OperationRecordFlowService flowService;
    private final LineMessagingClient lineMessagingClient;

    private final String queueUrl = System.getenv("SQS_QUEUE_URL");

    @Scheduled(fixedDelay = 5000)
    public void pollMessages() {

        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(5)
                .waitTimeSeconds(10)
                .build();

        List<software.amazon.awssdk.services.sqs.model.Message> messages =
                sqsClient.receiveMessage(request).messages();

        for (var msg : messages) {
            processMessage(msg.body());

            sqsClient.deleteMessage(DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(msg.receiptHandle())
                    .build());
        }
    }

    // ★★★ テスト用の入口メソッド（これを追加する）
    public void handleMessage(String message) {
        processMessage(message);
    }

    private void processMessage(String message) {
        try {
            // SQS → DTO
            LineInputDto input = mapper.readValue(message, LineInputDto.class);

            log.info("Received from SQS: userId={}, text={}", input.getUserId(), input.getText());

            // FlowService を呼ぶ
            String reply = flowService.handleInput(input.getUserId(), input.getText());

            log.info("FlowService reply: {}", reply);

            // LINE に返信（pushMessage に変更）
            lineMessagingClient.pushMessage(
                new PushMessage(input.getUserId(), new TextMessage(reply))
            );

            log.info("Reply sent to LINE");

        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
        }
    }
}
