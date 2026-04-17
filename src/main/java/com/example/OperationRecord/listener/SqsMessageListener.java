package com.example.OperationRecord.listener;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.OperationRecord.domain.OperationRecord;
import com.example.OperationRecord.dto.OperationRecordRequest;
import com.example.OperationRecord.entity.OperationRecordEntity;
import com.example.OperationRecord.mapper.OperationRecordMapper;
import com.example.OperationRecord.repository.OperationRecordJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

@Component
@RequiredArgsConstructor
public class SqsMessageListener {

    private final SqsClient sqsClient;
    private final ObjectMapper mapper;
    private final OperationRecordJpaRepository repository;

    private final String queueUrl = System.getenv("SQS_QUEUE_URL");

    // 5秒ごとに SQS をポーリング
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

            // 正常に処理できたら削除
            sqsClient.deleteMessage(DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(msg.receiptHandle())
                    .build());
        }
    }

    // ★ テスト用の public メソッド（入口）
    public void handleMessage(String message) {
        processMessage(message);
    }

    private void processMessage(String message) {
        try {
            // JSON → Request DTO
            OperationRecordRequest request =
                    mapper.readValue(message, OperationRecordRequest.class);

            // Request → Domain
            OperationRecord domain =
                    OperationRecordMapper.fromRequestToDomain(request);

            // Domain → Entity
            OperationRecordEntity entity =
                    OperationRecordMapper.fromDomainToEntity(domain);

            // DB 保存
            repository.save(entity);

            System.out.println("Saved record: " + entity);

        } catch (Exception e) {
            System.err.println("Error processing message: " + message);
            e.printStackTrace();
        }
    }
}
