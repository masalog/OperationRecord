package com.example.OperationRecord.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OperationRecordForm {

    // 運転手ID（Quick Reply / Postback で選択）
    @NotBlank(message = "運転手を選択してください")
    private String driverId;

    // 車両ID（Quick Reply / Postback で選択）
    @NotBlank(message = "車両を選択してください")
    private String vehicleId;

    // 開始日時（Datetime Picker → "2026-04-18 09:00"）
    @NotBlank(message = "開始日時を選択してください")
    private String startDateTime;

    // 終了日時（Datetime Picker → "2026-04-18 18:00"）
    @NotBlank(message = "終了日時を選択してください")
    private String endDateTime;

    // 開始メーター（車両選択後に DB から自動取得）
    @NotBlank(message = "開始メーターが設定されていません")
    private String startMeter;

    // 終了メーター（ユーザー入力：半角数字 or 全角数字）
    @NotBlank(message = "終了メーターを入力してください")
    @Pattern(
        regexp = "^[0-9０-９]+$",
        message = "終了メーターは半角数字または全角数字で入力してください"
    )
    private String endMeter;

    // 燃費（Quick Reply / Postback で選択）
    @NotBlank(message = "燃費を選択してください")
    private String fuelRate;
}

