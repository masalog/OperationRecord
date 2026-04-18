package com.example.OperationRecord.service.lineReply;

import java.util.Map;

public interface LineReplyService {
    void reply(String replyToken, Map<String, Object> message);
}

