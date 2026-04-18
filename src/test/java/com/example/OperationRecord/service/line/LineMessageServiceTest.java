package com.example.OperationRecord.service.line;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LineMessageServiceTest {

    @Test
    void 日時選択メッセージ送信時に_pushMessageが呼ばれ_宛先userIdが正しい() throws Exception {
        // arrange
        LineMessagingClient client = mock(LineMessagingClient.class);
        when(client.pushMessage(any(PushMessage.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        LineMessageService service = new LineMessageService(client);
        String userId = "U123";

        // act
        service.sendDateTimePicker(userId);

        // assert: pushMessageが呼ばれた & 宛先が一致
        ArgumentCaptor<PushMessage> captor = ArgumentCaptor.forClass(PushMessage.class);
        verify(client, times(1)).pushMessage(captor.capture());

        PushMessage sent = captor.getValue();
        assertEquals(userId, sent.getTo());
    }
}
