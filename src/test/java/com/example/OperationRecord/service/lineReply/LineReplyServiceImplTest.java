package com.example.OperationRecord.service.lineReply;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

class LineReplyServiceImplTest {

    @InjectMocks
    private LineReplyServiceImpl lineReplyService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // ★ private フィールドに値を注入（@Value の代わり）
        ReflectionTestUtils.setField(lineReplyService, "channelAccessToken", "TEST_TOKEN");
        ReflectionTestUtils.setField(lineReplyService, "replyUrl", "https://api.line.me/v2/bot/message/reply");
    }

    @Test
    void リプライAPIが正しく呼び出されること() {

        // RestTemplate の戻り値をモック
        when(restTemplate.postForEntity(
                any(String.class),
                any(HttpEntity.class),
                ArgumentMatchers.<Class<String>>any()
        )).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // テスト対象のメソッドを実行
        String replyToken = "REPLY_TOKEN";
        Map<String, Object> message = Map.of(
                "type", "text",
                "text", "テストメッセージ"
        );

        lineReplyService.reply(replyToken, message);

        // RestTemplate が正しく呼ばれたか検証
        verify(restTemplate).postForEntity(
                any(String.class),
                any(HttpEntity.class),
                ArgumentMatchers.<Class<String>>any()
        );
    }
}
