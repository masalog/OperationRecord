package com.example.OperationRecord.service.flow;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.OperationRecord.dto.OperationRecordForm;

class OperationRecordStepManagerTest {

    private final OperationRecordStepManager stepManager = new OperationRecordStepManager();

    @Test
    void 初期ステップは1である() {
        String userId = "user1";
        assertEquals(1, stepManager.getStep(userId));
    }

    @Test
    void nextStepでステップが進む() {
        String userId = "user1";

        stepManager.nextStep(userId);
        assertEquals(2, stepManager.getStep(userId));

        stepManager.nextStep(userId);
        assertEquals(3, stepManager.getStep(userId));
    }

    @Test
    void getFormで同じフォームが返される() {
        String userId = "user1";

        OperationRecordForm form1 = stepManager.getForm(userId);
        OperationRecordForm form2 = stepManager.getForm(userId);

        assertSame(form1, form2); // 同じインスタンスであること
    }

    @Test
    void resetでステップとフォームが初期化される() {
        String userId = "user1";

        stepManager.nextStep(userId); // ステップ2へ
        stepManager.getForm(userId).setVehicleId("10");

        stepManager.reset(userId);

        // ステップが1に戻る
        assertEquals(1, stepManager.getStep(userId));

        // フォームが新しくなる
        OperationRecordForm newForm = stepManager.getForm(userId);
        assertNull(newForm.getVehicleId());
    }
}
