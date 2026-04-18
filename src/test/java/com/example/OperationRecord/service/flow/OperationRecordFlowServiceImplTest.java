package com.example.OperationRecord.service.flow;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.OperationRecord.dto.OperationRecordRequest;
import com.example.OperationRecord.dto.OperationRecordResponse;
import com.example.OperationRecord.dto.OperationRecordForm;
import com.example.OperationRecord.service.application.OperationRecordApplicationService;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

class OperationRecordFlowServiceImplTest {

    // フェイク StepManager
    static class FakeStepManager extends OperationRecordStepManager {

        private int step = 1;
        private OperationRecordForm form = new OperationRecordForm();
        boolean nextStepCalled = false;
        boolean resetCalled = false;

        @Override
        public int getStep(String userId) {
            return step;
        }

        @Override
        public void nextStep(String userId) {
            nextStepCalled = true;
            step++;
        }

        @Override
        public OperationRecordForm getForm(String userId) {
            return form;
        }

        @Override
        public void reset(String userId) {
            resetCalled = true;
        }
    }

    // フェイク ApplicationService
    static class FakeApplicationService implements OperationRecordApplicationService {

        boolean registerCalled = false;

        @Override
        public OperationRecordResponse register(OperationRecordRequest request) {
            registerCalled = true;
            return new OperationRecordResponse(
                    1L, 10L, 20L, null, null, 1000, 1200, 5.0
            );
        }
    }

    // テスト①：ステップ1
    @Test
    void ステップ1で車両IDを受け取り次の質問を返す() {

        FakeStepManager stepManager = new FakeStepManager();
        FakeApplicationService appService = new FakeApplicationService();

        OperationRecordFlowServiceImpl flowService =
                new OperationRecordFlowServiceImpl(appService, stepManager);

        // ★ 戻り値は Message
        Message result = flowService.handleInput("user1", "10");

        // TextMessage が返ってくること
        assertTrue(result instanceof TextMessage);

        // 中身のテキストを検証
        assertEquals(
                "運転手IDを入力してください",
                ((TextMessage) result).getText()
        );

        // フォーム・ステップの状態確認
        assertEquals("10", stepManager.getForm("user1").getVehicleId());
        assertTrue(stepManager.nextStepCalled);
    }

    // テスト②：最終ステップ
    @Test
    void 最終ステップで登録処理が呼ばれ登録完了メッセージを返す() {

        FakeStepManager stepManager = new FakeStepManager();
        FakeApplicationService appService = new FakeApplicationService();

        // 最終ステップに設定
        stepManager.step = 7;

        // フォームに途中の値をセット
        OperationRecordForm form = stepManager.getForm("user1");
        form.setVehicleId("10");
        form.setDriverId("20");
        form.setStartDateTime("2026-01-01T10:00");
        form.setEndDateTime("2026-01-01T12:00");
        form.setStartMeter("1000");
        form.setEndMeter("1200");

        OperationRecordFlowServiceImpl flowService =
                new OperationRecordFlowServiceImpl(appService, stepManager);

        // ★ 戻り値は Message
        Message result = flowService.handleInput("user1", "5.0");

        assertTrue(result instanceof TextMessage);

        String text = ((TextMessage) result).getText();

        assertTrue(text.contains("登録が完了しました"));
        assertTrue(appService.registerCalled);
        assertTrue(stepManager.resetCalled);
    }
}