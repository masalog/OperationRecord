package com.example.OperationRecord.service;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.stereotype.Service;

@Service
public class SqsSendService {

    private final SqsTemplate sqsTemplate;

    public SqsSendService(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    public void send(String queueName, String message) {
        sqsTemplate.send(queueName, message);
    }
}
