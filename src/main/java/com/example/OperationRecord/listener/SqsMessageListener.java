package com.example.OperationRecord.listener;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.OperationRecord.dto.LineInputDto;
import com.example.OperationRecord.service.flow.OperationRecordFlowService;
import com.example.OperationRecord.service.flow.OperationRecordStepManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsMessageListener {

    private final SqsClient sqsClient;
    private final ObjectMapper mapper;
    private final OperationRecordFlowService flowService;
    private final OperationRecordStepManager stepManager; // ★追加
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

            sqsClient.deleteMessage(
                DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(msg.receiptHandle())
                    .build()
            );
        }
    }

    // テスト用入口
    public void handleMessage(String message) {
        processMessage(message);
    }

    private void processMessage(String message) {
        try {
            LineInputDto input = mapper.readValue(message, LineInputDto.class);
            String inputText = pickInputText(input);

            log.info("Received from SQS: userId={}, inputText={}",
                    input.getUserId(), inputText);

            // ===============================
            // トリガー：会話開始「記録」
            // ===============================
            if ("記録".equals(inputText)) {
                // step を確実に初期化（=1）
                stepManager.reset(input.getUserId());

                // 最初の案内
                lineMessagingClient.pushMessage(
                        new PushMessage(
                                input.getUserId(),
                                new TextMessage("車両IDを入力してください")
                        )
                );
                return; // ★ Flow は呼ばない
            }
            // ===============================

            if (inputText == null || inputText.isBlank()) {
                Message fallback =
                        new TextMessage("入力を受け取れませんでした。もう一度送ってください。");
                lineMessagingClient.pushMessage(
                        new PushMessage(input.getUserId(), fallback)
                );
                return;
            }

            // 通常入力はすべて Flow に渡す
            Message reply =
                    flowService.handleInput(input.getUserId(), inputText);

            lineMessagingClient.pushMessage(
                    new PushMessage(input.getUserId(), reply)
            );

        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
        }
    }

    private String pickInputText(LineInputDto input) {
        if (input.getPostbackDatetime() != null && !input.getPostbackDatetime().isBlank()) {
            return input.getPostbackDatetime().trim();
        }
        if (input.getText() != null && !input.getText().isBlank()) {
            return input.getText().trim();
        }
        return null;
    }
}