package com.example.OperationRecord.listener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.OperationRecord.dto.LineInputDto;
import com.example.OperationRecord.service.flow.OperationRecordFlowService;
import com.example.OperationRecord.service.flow.OperationRecordStepManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

@Slf4j
@Component
@ConditionalOnProperty(name = "aws.sqs.enabled", havingValue = "true")
public class SqsMessageListener {

    private final SqsClient sqsClient;
    private final ObjectMapper mapper;
    private final OperationRecordFlowService flowService;
    private final OperationRecordStepManager stepManager;
    private final LineMessagingClient lineMessagingClient;
    private final String queueUrl;

    public SqsMessageListener(
            SqsClient sqsClient,
            ObjectMapper mapper,
            OperationRecordFlowService flowService,
            OperationRecordStepManager stepManager,
            LineMessagingClient lineMessagingClient,
            @Value("${SQS_QUEUE_URL}") String queueUrl
    ) {
        this.sqsClient = sqsClient;
        this.mapper = mapper;
        this.flowService = flowService;
        this.stepManager = stepManager;
        this.lineMessagingClient = lineMessagingClient;

        if (queueUrl == null || queueUrl.isBlank()) {
            throw new IllegalStateException("SQS_QUEUE_URL is not set");
        }
        this.queueUrl = queueUrl;
    }

    @Scheduled(fixedDelayString = "${aws.sqs.poll.delay-ms:5000}")
    public void pollMessages() {

        var request = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(5)
                .waitTimeSeconds(10)
                .build();

        var messages = sqsClient.receiveMessage(request).messages();
        if (messages == null || messages.isEmpty()) return;

        for (var msg : messages) {
            boolean ok = false;
            try {
                ok = processMessage(msg.body());
            } catch (Exception e) {
                log.error("Error processing SQS messageId={} body={}", msg.messageId(), msg.body(), e);
            }

            if (ok) {
                deleteMessage(msg);
            }
        }
    }

    private void deleteMessage(software.amazon.awssdk.services.sqs.model.Message msg) {
        try {
            sqsClient.deleteMessage(
                    DeleteMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .receiptHandle(msg.receiptHandle())
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to delete SQS messageId={}", msg.messageId(), e);
        }
    }

    public boolean handleMessage(String message) {
        return processMessage(message);
    }

    private boolean processMessage(String message) {
        try {
            var input = mapper.readValue(message, LineInputDto.class);
            var inputText = pickInputText(input);

            log.info("Received from SQS: userId={}, inputText={}", input.getUserId(), inputText);

            if ("記録".equals(inputText)) {
                stepManager.reset(input.getUserId());
                pushText(input.getUserId(), "車両IDを入力してください");
                return true;
            }

            if (inputText == null || inputText.isBlank()) {
                pushText(input.getUserId(), "入力を受け取れませんでした。もう一度送ってください。");
                return true;
            }

            var reply = flowService.handleInput(input.getUserId(), inputText);
            pushMessage(input.getUserId(), reply);

            return true;

        } catch (Exception e) {
            log.error("processMessage failed. raw={}", message, e);
            return false;
        }
    }

    private void pushText(String userId, String text) {
        pushMessage(userId, new TextMessage(text));
    }

    private void pushMessage(String userId, Message message) {
        lineMessagingClient.pushMessage(new PushMessage(userId, message))
                .exceptionally(ex -> {
                    log.error("LINE push failed userId={}", userId, ex);
                    return null;
                });
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
