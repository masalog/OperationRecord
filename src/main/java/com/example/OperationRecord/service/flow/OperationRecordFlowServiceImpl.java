package com.example.OperationRecord.service.flow;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.OperationRecord.dto.OperationRecordForm;
import com.example.OperationRecord.dto.OperationRecordRequest;
import com.example.OperationRecord.mapper.OperationRecordFormMapper;
import com.example.OperationRecord.service.application.OperationRecordApplicationService;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OperationRecordFlowServiceImpl implements OperationRecordFlowService {

    private final OperationRecordApplicationService applicationService;
    private final OperationRecordStepManager stepManager;

    @Override
    public Message handleInput(String userId, String input) {

        int step = stepManager.getStep(userId);
        OperationRecordForm form = stepManager.getForm(userId);

        switch (step) {

            // ① 車両ID（最初の入力）
            case 1 -> {
                form.setVehicleId(input);
                stepManager.nextStep(userId);
                return new TextMessage("運転手IDを入力してください");
            }

            // ② 運転手ID
            case 2 -> {
                form.setDriverId(input);
                stepManager.nextStep(userId);
                return buildStartDatetimePicker();
            }

            // ③ 開始日時
            case 3 -> {
                form.setStartDateTime(input);
                stepManager.nextStep(userId);
                return buildEndDatetimePicker();
            }

            // ④ 終了日時
            case 4 -> {
                form.setEndDateTime(input);
                stepManager.nextStep(userId);
                return new TextMessage("開始メーターを入力してください");
            }

            // ⑤ 開始メーター
            case 5 -> {
                form.setStartMeter(input);
                stepManager.nextStep(userId);
                return new TextMessage("終了メーターを入力してください");
            }

            // ⑥ 終了メーター
            case 6 -> {
                form.setEndMeter(input);
                stepManager.nextStep(userId);
                return new TextMessage("燃費を入力してください");
            }

            // ⑦ 燃費 → 登録
            case 7 -> {
                form.setFuelRate(input);

                // ✅ 登録処理
                OperationRecordRequest request =
                    OperationRecordFormMapper.fromFormToRequest(form);
                applicationService.register(request);

                // ✅ state を完全にリセット（ここが最重要）
                stepManager.reset(userId);

                // ✅ 次の行動を明示
                return new TextMessage("""
                    登録が完了しました
                    続けて登録する場合は「記録」と入力してください""");
                    
            }

            default -> {
                stepManager.reset(userId);
                return new TextMessage("エラーが発生しました。最初からやり直してください。");
            }
        }
    }

    // --- DatetimePicker builders ---

    private Message buildStartDatetimePicker() {
        var action =
            DatetimePickerAction.OfLocalDatetime.builder()
                .label("開始日時を選択")
                .data("action=startDateTime")
                .build();

        var template = new ButtonsTemplate(
            null,
            "開始日時",
            "開始日時を選択してください",
            List.of(action)
        );

        return new TemplateMessage("開始日時を選択してください", template);
    }

    private Message buildEndDatetimePicker() {
        var action =
            DatetimePickerAction.OfLocalDatetime.builder()
                .label("終了日時を選択")
                .data("action=endDateTime")
                .build();

        var template = new ButtonsTemplate(
            null,
            "終了日時",
            "終了日時を選択してください",
            List.of(action)
        );

        return new TemplateMessage("終了日時を選択してください", template);
    }
}