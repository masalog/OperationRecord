package com.example.OperationRecord.service.flow;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.OperationRecord.dto.OperationRecordForm;

class OperationRecordStepManagerTest {

    private final OperationRecordStepManager stepManager = new OperationRecordStepManager();

    @Test
    void 初期ステップは1である() {
        String userId = "user1";
        assertEquals(1, stepManager.getCurrentOperationInputStep(userId));
    }

    @Test
    void nextStepでステップが進む() {
        String userId = "user1";

        stepManager.advanceOperationInputStep(userId);
        assertEquals(2, stepManager.getCurrentOperationInputStep(userId));

        stepManager.advanceOperationInputStep(userId);
        assertEquals(3, stepManager.getCurrentOperationInputStep(userId));
    }

    @Test
    void getFormで同じフォームが返される() {
        String userId = "user1";

        OperationRecordForm form1 = stepManager.getOrCreateOperationInputForm(userId);
        OperationRecordForm form2 = stepManager.getOrCreateOperationInputForm(userId);

        assertSame(form1, form2); // 同じインスタンスであること
    }

    @Test
    void resetでステップとフォームと保存IDが初期化される() {
        String userId = "user1";

        // ステップを進める
        stepManager.advanceOperationInputStep(userId); // ステップ2へ

        // フォームに値を入れる
        stepManager.getOrCreateOperationInputForm(userId).setVehicleId("10");

        // 保存IDを入れる
        stepManager.storeSavedOperationRecordId(userId, 99L);

        // リセット実行
        stepManager.resetOperationInputState(userId);

        // ステップが1に戻る
        assertEquals(1, stepManager.getCurrentOperationInputStep(userId));

        // フォームが新しくなる
        OperationRecordForm newForm = stepManager.getOrCreateOperationInputForm(userId);
        assertNull(newForm.getVehicleId());

        // 保存IDも消えている
        assertNull(stepManager.getSavedOperationRecordId(userId));
    }
}
