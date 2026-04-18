package com.example.OperationRecord.service.flow;

import org.springframework.stereotype.Service;

import com.example.OperationRecord.dto.OperationRecordRequest;
import com.example.OperationRecord.dto.OperationRecordResponse;
import com.example.OperationRecord.dto.OperationRecordForm;
import com.example.OperationRecord.mapper.OperationRecordFormMapper;
import com.example.OperationRecord.service.application.OperationRecordApplicationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OperationRecordFlowServiceImpl implements OperationRecordFlowService {

    private final OperationRecordApplicationService applicationService;
    private final OperationRecordStepManager stepManager;

    @Override
    public String handleInput(String userId, String input) {

        int step = stepManager.getStep(userId);
        OperationRecordForm form = stepManager.getForm(userId);

        switch (step) {

            case 1 -> {
                form.setVehicleId(input);
                stepManager.nextStep(userId);
                return "運転手IDを入力してください";
            }

            case 2 -> {
                form.setDriverId(input);
                stepManager.nextStep(userId);
                return "開始日時を入力してください（例：2024-01-01 10:00）";
            }

            case 3 -> {
                form.setStartDateTime(input);
                stepManager.nextStep(userId);
                return "終了日時を入力してください";
            }

            case 4 -> {
                form.setEndDateTime(input);
                stepManager.nextStep(userId);
                return "開始メーターを入力してください";
            }

            case 5 -> {
                form.setStartMeter(input);
                stepManager.nextStep(userId);
                return "終了メーターを入力してください";
            }

            case 6 -> {
                form.setEndMeter(input);
                stepManager.nextStep(userId);
                return "燃費を入力してください";
            }

            case 7 -> {
                form.setFuelRate(input);

                // Form → Request
                OperationRecordRequest request = OperationRecordFormMapper.fromFormToRequest(form);

                // 登録処理
                OperationRecordResponse response = applicationService.register(request);

                // 状態リセット
                stepManager.reset(userId);

                return "登録が完了しました（ID: " + response.getId() + "）";
            }

            default -> {
                stepManager.reset(userId);
                return "エラーが発生しました。最初からやり直してください。";
            }
        }
    }
}

