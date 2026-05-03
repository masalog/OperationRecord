package com.example.OperationRecord.service.flow;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.example.OperationRecord.dto.OperationRecordForm;

@Component
public class OperationRecordStepManager {

    // ★ ユーザーごとの「運行記録入力の現在ステップ」
    private final Map<String, Integer> userOperationInputStep = new ConcurrentHashMap<>();

    // ★ ユーザーごとの「運行記録入力途中データ」
    private final Map<String, OperationRecordForm> userOperationInputForm = new ConcurrentHashMap<>();

    // ★ ユーザーごとの「前半入力で保存した運行記録ID」
    private final Map<String, Long> userSavedOperationRecordId = new ConcurrentHashMap<>();


    /** 現在の運行記録入力ステップを取得（未開始なら1） */
    public int getCurrentOperationInputStep(String userId) {
        return userOperationInputStep.getOrDefault(userId, 1);
    }

    /** 運行記録入力ステップを1つ進める */
    public void advanceOperationInputStep(String userId) {
        userOperationInputStep.put(userId, getCurrentOperationInputStep(userId) + 1);
    }

    /** 入力途中の運行記録フォームを取得（なければ新規作成） */
    public OperationRecordForm getOrCreateOperationInputForm(String userId) {
        return userOperationInputForm.computeIfAbsent(userId, id -> new OperationRecordForm());
    }

    /** ★ 前半入力で保存した運行記録IDを保持 */
    public void storeSavedOperationRecordId(String userId, Long recordId) {
        userSavedOperationRecordId.put(userId, recordId);
    }

    /** ★ 前半入力で保存した運行記録IDを取得 */
    public Long getSavedOperationRecordId(String userId) {
        return userSavedOperationRecordId.get(userId);
    }

    /** ★ 前半保存済み運行記録IDを削除 */
    public void clearSavedOperationRecordId(String userId) {
        userSavedOperationRecordId.remove(userId);
    }

    /** ★ 運行記録入力に関する全データを初期化 */
    public void resetOperationInputState(String userId) {
        userOperationInputStep.remove(userId);
        userOperationInputForm.remove(userId);
        userSavedOperationRecordId.remove(userId);
    }
}
