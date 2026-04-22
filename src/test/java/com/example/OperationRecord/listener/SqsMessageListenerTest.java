package com.example.OperationRecord.listener;

import com.example.OperationRecord.service.flow.OperationRecordFlowService;
import com.example.OperationRecord.service.flow.OperationRecordStepManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import software.amazon.awssdk.services.sqs.SqsClient;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SqsMessageListenerTest {

    private final ObjectMapper mapper =
        new ObjectMapper().registerModule(new JavaTimeModule());

    private final OperationRecordFlowService flowService =
        mock(OperationRecordFlowService.class);

    private final LineMessagingClient lineClient =
        mock(LineMessagingClient.class);

    private final OperationRecordStepManager stepManager =
        mock(OperationRecordStepManager.class);

    private final SqsClient sqsClient =
        mock(SqsClient.class);

    private SqsMessageListener listener;

    @BeforeEach
    void 準備() {
        // pushMessage は CompletableFuture を返すのでダミーを返す
        when(lineClient.pushMessage(any(PushMessage.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        // ★ 新しいコンストラクタは queueUrl を String で直接渡す
        listener = new SqsMessageListener(
            sqsClient,
            mapper,
            flowService,
            stepManager,
            lineClient,
            "https://example.com/queue-url-for-test"
        );
    }

    @Test
    void 通常メッセージはフロー処理と返信送信が実行される() throws Exception {

        when(flowService.handleInput("U123", "10"))
                .thenReturn(new TextMessage("OK"));

        String json = """
        {
            "userId": "U123",
            "text": "10",
            "replyToken": "abcd"
        }
        """;

        listener.handleMessage(json);

        // フロー処理が呼ばれたか
        verify(flowService, times(1)).handleInput("U123", "10");

        // 返信送信（pushMessage）が呼ばれたか
        verify(lineClient, times(1)).pushMessage(any(PushMessage.class));

        // 記録コマンドではないので reset は呼ばれない
        verify(stepManager, never()).reset(anyString());
    }

    @Test
    void 記録コマンドは案内のみ送信されフロー処理は実行されない() throws Exception {

        String json = """
        {
            "userId": "U999",
            "text": "記録",
            "replyToken": "zzzz"
        }
        """;

        listener.handleMessage(json);

        // 記録は案内のみなのでフロー処理は呼ばれない
        verify(flowService, never()).handleInput(anyString(), anyString());

        // step 初期化が呼ばれる
        verify(stepManager, times(1)).reset("U999");

        // pushMessage が1回呼ばれる
        ArgumentCaptor<PushMessage> captor = ArgumentCaptor.forClass(PushMessage.class);
        verify(lineClient, times(1)).pushMessage(captor.capture());

        // 送った文言が期待どおりか
        PushMessage pushed = captor.getValue();
        TextMessage msg = (TextMessage) pushed.getMessages().get(0);
        assertEquals("車両IDを入力してください", msg.getText());
    }
}

