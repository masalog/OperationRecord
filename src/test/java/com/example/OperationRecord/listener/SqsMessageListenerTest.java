package com.example.OperationRecord.listener;

import com.example.OperationRecord.service.flow.OperationRecordFlowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SqsMessageListenerTest {

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private final OperationRecordFlowService flowService = mock(OperationRecordFlowService.class);
    private final LineMessagingClient lineClient = mock(LineMessagingClient.class);

    private final SqsMessageListener listener =
            new SqsMessageListener(null, mapper, flowService, lineClient);

    @Test
    void SQSメッセージを処理してFlowServiceとLINE返信が呼ばれる() throws Exception {

        // FlowService の戻り値：String ではなく Message
        when(flowService.handleInput("U123", "10"))
                .thenReturn(new TextMessage("OK"));

        // pushMessage は CompletableFuture を返すのでダミーを返しておく（例外回避）
        when(lineClient.pushMessage(any(PushMessage.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        String json = """
        {
            "userId": "U123",
            "text": "10",
            "replyToken": "abcd"
        }
        """;

        // テスト用の入口
        listener.handleMessage(json);

        // FlowService が呼ばれたか
        verify(flowService, times(1)).handleInput("U123", "10");

        // LINE返信（pushMessage）が呼ばれたか
        verify(lineClient, times(1)).pushMessage(any(PushMessage.class));
    }
}