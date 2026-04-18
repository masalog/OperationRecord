package com.example.OperationRecord.service.flow;


import com.linecorp.bot.model.message.Message;

public interface OperationRecordFlowService {
    Message handleInput(String userId, String input);
}

