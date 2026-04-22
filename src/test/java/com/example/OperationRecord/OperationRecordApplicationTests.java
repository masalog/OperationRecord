package com.example.OperationRecord;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("このアプリは外部設定・DB・LINE Bot に依存するため contextLoads は不要")
class OperationRecordApplicationTests {

    @Test
    void contextLoads() {
    }
}