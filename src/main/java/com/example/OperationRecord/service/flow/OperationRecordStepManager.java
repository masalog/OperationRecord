package com.example.OperationRecord.service.flow;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.example.OperationRecord.dto.OperationRecordForm;

@Component
public class OperationRecordStepManager {

    // ユーザーごとの現在ステップ
    private final Map<String, Integer> stepMap = new ConcurrentHashMap<>();

    // ユーザーごとの入力途中フォーム
    private final Map<String, OperationRecordForm> formMap = new ConcurrentHashMap<>();

    /** 現在のステップを取得（未登録なら1） */
    public int getStep(String userId) {
        return stepMap.getOrDefault(userId, 1);
    }

    /** 次のステップへ進める */
    public void nextStep(String userId) {
        stepMap.put(userId, getStep(userId) + 1);
    }

    /** フォームを取得（なければ新規作成） */
    public OperationRecordForm getForm(String userId) {
        return formMap.computeIfAbsent(userId, id -> new OperationRecordForm());
    }

    /** ステップとフォームを初期化 */
    public void reset(String userId) {
        stepMap.remove(userId);
        formMap.remove(userId);
    }

}

