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

    private static final String RETURN_TRIGGER = "帰還";

    private final OperationRecordApplicationService applicationService;
    private final OperationRecordStepManager stepManager;

    @Override
    public Message handleInput(String userId, String input) {

        int step = stepManager.getCurrentOperationInputStep(userId);
        OperationRecordForm form = stepManager.getOrCreateOperationInputForm(userId);

        switch (step) {

            // ① 車両ID
            case 1 -> {
                form.setVehicleId(input);
                stepManager.advanceOperationInputStep(userId);
                return new TextMessage("運転手IDを入力してください");
            }

            // ② 運転手ID
            case 2 -> {
                form.setDriverId(input);
                stepManager.advanceOperationInputStep(userId);
                return buildStartDatetimePicker();
            }

            // ③ 開始日時 → 次は開始メーター
            case 3 -> {
                form.setStartDateTime(input);
                stepManager.advanceOperationInputStep(userId); // 3 -> 4
                return new TextMessage("開始メーターを入力してください");
            }

            // ④ 開始メーター → ★前半保存(register) → ★待機へ
            case 4 -> {
                form.setStartMeter(input);

                OperationRecordRequest request = OperationRecordFormMapper.toRegisterRequest(form);
                var response = applicationService.register(request);

                stepManager.storeSavedOperationRecordId(userId, response.getId());
                stepManager.advanceOperationInputStep(userId); // 4 -> 5

                return new TextMessage("""
                        出庫前の入力が完了しました。
                        帰還時に「帰還」と入力して続きを登録してください。
                        """);
            }

            // ⑤ 待機（帰還トリガー待ち）
            case 5 -> {
                if (!RETURN_TRIGGER.equals(input.trim())) {
                    return new TextMessage("""
                            現在、帰還入力待ちです。
                            帰還したら「帰還」と入力してください。
                            """);
                }

                stepManager.advanceOperationInputStep(userId); // 5 -> 6
                return buildEndDatetimePicker();
            }

            // ⑥ 終了日時
            case 6 -> {
                form.setEndDateTime(input);
                stepManager.advanceOperationInputStep(userId); // 6 -> 7
                return new TextMessage("終了メーターを入力してください");
            }

            // ⑦ 終了メーター → ★後半更新(update) → 完了
            case 7 -> {
                form.setEndMeter(input);

                Long savedId = stepManager.getSavedOperationRecordId(userId);
                OperationRecordRequest request = OperationRecordFormMapper.toUpdateRequest(form, savedId);

                applicationService.update(request);

                stepManager.resetOperationInputState(userId);

                return new TextMessage("""
                        登録が完了しました。
                        続けて登録する場合は「記録」と入力してください。
                        """);
            }

            default -> {
                stepManager.resetOperationInputState(userId);
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