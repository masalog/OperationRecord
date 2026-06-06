package com.example.OperationRecord.mapper;

import com.example.OperationRecord.dto.OperationRecordRequest;
import com.example.OperationRecord.dto.OperationRecordForm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.example.OperationRecord.exception.BadRequestException;

public class OperationRecordFormMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is null/blank");
        }
        return value.trim();
    }

    private static LocalDateTime parseLocalDateTime(String value, String fieldName) {
        String v = requireText(value, fieldName);
        try {
            return LocalDateTime.parse(v, FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(fieldName + " format is invalid: " + v, ex);
        }
    }

    /**
     * 全角数字→半角数字に正規化（IDやメーターなど「数字のみ」想定の入力用）
     */
    private static String normalizeDigits(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        for (char c : text.toCharArray()) {
            if (c >= '０' && c <= '９') {
                sb.append((char) (c - '０' + '0'));
            } else {
                sb.append(c);
            }
        }
        return sb.toString().trim();
    }

    /**
     * 数字だけを許可して long にする（全角数字も許容）
     */
    private static long toLongNumber(String text, String fieldName) {
        String v = requireText(text, fieldName);
        String normalized = normalizeDigits(v);

        if (!normalized.matches("\\d+")) {
            throw new IllegalArgumentException(fieldName + " must be numeric: " + normalized);
        }

        try {
            return Long.parseLong(normalized);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " is out of range: " + normalized, ex);
        }
    }

    /**
     * 数字だけを許可して int にする（全角数字も許容）
     */
    private static int toIntNumber(String text, String fieldName) {
        String v = requireText(text, fieldName);
        String normalized = normalizeDigits(v);

        if (!normalized.matches("\\d+")) {
            throw new IllegalArgumentException(fieldName + " must be numeric: " + normalized);
        }

        try {
            return Integer.parseInt(normalized);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " is out of range: " + normalized, ex);
        }
    }

    /**
     * 前半保存（register）用：
     * endDateTime / endMeter は未入力でOK
     * startMeter は前半で必須
     */
    public static OperationRecordRequest toRegisterRequest(OperationRecordForm form) {
        if (form == null) throw new IllegalArgumentException("form is null");

        OperationRecordRequest req = new OperationRecordRequest();
        req.setOperationRecordId(null);

        // ★ driverId / vehicleId も全角数字を許容
        req.setDriverId(toLongNumber(form.getDriverId(), "driverId"));
        req.setVehicleId(toLongNumber(form.getVehicleId(), "vehicleId"));

        req.setStartDateTime(parseLocalDateTime(form.getStartDateTime(), "startDateTime"));
        req.setStartMeter(toIntNumber(form.getStartMeter(), "startMeter"));

        return req;
    }

    /**
     * 後半更新（update）用：
     * endDateTime / endMeter を必須チェック
     * startMeter は前半で入力済みの値をフォームからコピーして載せる
     */
    public static OperationRecordRequest toUpdateRequest(OperationRecordForm form, Long operationRecordId) {
        if (form == null) throw new IllegalArgumentException("form is null");
        if (operationRecordId == null) throw new IllegalArgumentException("operationRecordId is null");

        OperationRecordRequest req = new OperationRecordRequest();
        req.setOperationRecordId(operationRecordId);

        // ★ driverId / vehicleId も全角数字を許容
        req.setDriverId(toLongNumber(form.getDriverId(), "driverId"));
        req.setVehicleId(toLongNumber(form.getVehicleId(), "vehicleId"));

        req.setStartDateTime(parseLocalDateTime(form.getStartDateTime(), "startDateTime"));
        req.setStartMeter(toIntNumber(form.getStartMeter(), "startMeter"));

        req.setEndDateTime(parseLocalDateTime(form.getEndDateTime(), "endDateTime"));
        req.setEndMeter(toIntNumber(form.getEndMeter(), "endMeter"));

        return req;
    }
}
