package com.example.OperationRecord.service.flow;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.OperationRecord.dto.OperationRecordForm;
import com.example.OperationRecord.dto.OperationRecordRequest;
import com.example.OperationRecord.dto.OperationRecordResponse;
import com.example.OperationRecord.service.application.OperationRecordApplicationService;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;

class OperationRecordFlowServiceImplTest {

    /** フェイク StepManager（確定フロー対応） */
    static class FakeStepManager extends OperationRecordStepManager {

        int step = 1;
        OperationRecordForm form = new OperationRecordForm();

        boolean nextStepCalled = false;
        boolean resetCalled = false;

        Long savedId = null;

        @Override
        public int getCurrentOperationInputStep(String userId) {
            return step;
        }

        @Override
        public void advanceOperationInputStep(String userId) {
            nextStepCalled = true;
            step++;
        }

        @Override
        public OperationRecordForm getOrCreateOperationInputForm(String userId) {
            return form;
        }

        @Override
        public void resetOperationInputState(String userId) {
            resetCalled = true;
            step = 1;
            form = new OperationRecordForm();
            savedId = null;
        }

        @Override
        public void storeSavedOperationRecordId(String userId, Long id) {
            savedId = id;
        }

        @Override
        public Long getSavedOperationRecordId(String userId) {
            return savedId;
        }
    }

    /** フェイク ApplicationService（燃費なし） */
    static class FakeApplicationService implements OperationRecordApplicationService {

        boolean registerCalled = false;
        boolean updateCalled = false;

        @Override
        public OperationRecordResponse register(OperationRecordRequest request) {
            registerCalled = true;
            return new OperationRecordResponse(
                    1L,
                    request.getVehicleId(),
                    request.getDriverId(),
                    request.getStartDateTime(),
                    null,
                    request.getStartMeter(),
                    null
            );
        }

        @Override
        public OperationRecordResponse update(OperationRecordRequest request) {
            updateCalled = true;
            return new OperationRecordResponse(
                    request.getOperationRecordId(),
                    request.getVehicleId(),
                    request.getDriverId(),
                    request.getStartDateTime(),
                    request.getEndDateTime(),
                    request.getStartMeter(),
                    request.getEndMeter()
            );
        }
    }

    @Test
    void 車両番号を入力すると運転手番号の入力が求められる() {

        FakeStepManager stepManager = new FakeStepManager();
        FakeApplicationService appService = new FakeApplicationService();

        OperationRecordFlowServiceImpl flow =
                new OperationRecordFlowServiceImpl(appService, stepManager);

        Message result = flow.handleInput("user", "10");

        assertInstanceOf(TextMessage.class, result);
        assertEquals("運転手IDを入力してください", ((TextMessage) result).getText());
        assertEquals(2, stepManager.step);
    }

    @Test
    void 開始日時入力後に開始メーター入力へ進む() {

        FakeStepManager stepManager = new FakeStepManager();
        FakeApplicationService appService = new FakeApplicationService();

        stepManager.step = 3;
        stepManager.form.setVehicleId("10");
        stepManager.form.setDriverId("20");

        OperationRecordFlowServiceImpl flow =
                new OperationRecordFlowServiceImpl(appService, stepManager);

        Message result = flow.handleInput("user", "2026-01-01T10:00");

        assertInstanceOf(TextMessage.class, result);
        assertEquals("開始メーターを入力してください", ((TextMessage) result).getText());
        assertEquals(4, stepManager.step);
        assertFalse(appService.registerCalled);
    }

    @Test
    void 開始メーター入力で前半保存され待機に入る() {

        FakeStepManager stepManager = new FakeStepManager();
        FakeApplicationService appService = new FakeApplicationService();

        stepManager.step = 4;
        stepManager.form.setVehicleId("10");
        stepManager.form.setDriverId("20");
        stepManager.form.setStartDateTime("2026-01-01T10:00");

        OperationRecordFlowServiceImpl flow =
                new OperationRecordFlowServiceImpl(appService, stepManager);

        Message result = flow.handleInput("user", "1000");

        assertInstanceOf(TextMessage.class, result);
        assertTrue(((TextMessage) result).getText().contains("出庫前の入力が完了しました"));
        assertTrue(appService.registerCalled);
        assertEquals(5, stepManager.step);
        assertEquals(1L, stepManager.getSavedOperationRecordId("user"));
    }

    @Test
    void 待機中に帰還以外を入力すると案内のみ返る() {

        FakeStepManager stepManager = new FakeStepManager();
        FakeApplicationService appService = new FakeApplicationService();

        stepManager.step = 5;

        OperationRecordFlowServiceImpl flow =
                new OperationRecordFlowServiceImpl(appService, stepManager);

        Message result = flow.handleInput("user", "こんにちは");

        assertInstanceOf(TextMessage.class, result);
        assertTrue(((TextMessage) result).getText().contains("帰還したら"));
        assertEquals(5, stepManager.step);
    }

    @Test
    void 帰還入力で終了日時ピッカーへ進む() {

        FakeStepManager stepManager = new FakeStepManager();
        FakeApplicationService appService = new FakeApplicationService();

        stepManager.step = 5;

        OperationRecordFlowServiceImpl flow =
                new OperationRecordFlowServiceImpl(appService, stepManager);

        Message result = flow.handleInput("user", "帰還");

        assertInstanceOf(TemplateMessage.class, result);
        assertEquals(6, stepManager.step);
    }

    @Test
    void 終了メーター入力で後半更新され完了する() {

        FakeStepManager stepManager = new FakeStepManager();
        FakeApplicationService appService = new FakeApplicationService();

        stepManager.step = 7;
        stepManager.savedId = 1L;

        OperationRecordForm form = stepManager.getOrCreateOperationInputForm("user");
        form.setVehicleId("10");
        form.setDriverId("20");
        form.setStartDateTime("2026-01-01T10:00");
        form.setStartMeter("1000");
        form.setEndDateTime("2026-01-01T12:00");

        OperationRecordFlowServiceImpl flow =
                new OperationRecordFlowServiceImpl(appService, stepManager);

        Message result = flow.handleInput("user", "1200");

        assertInstanceOf(TextMessage.class, result);
        assertTrue(((TextMessage) result).getText().contains("登録が完了しました"));
        assertTrue(appService.updateCalled);
        assertTrue(stepManager.resetCalled);
        assertEquals(1, stepManager.step);
    }
}
