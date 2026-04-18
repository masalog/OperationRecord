package com.example.OperationRecord.mapper;

import com.example.OperationRecord.dto.OperationRecordRequest;
import com.example.OperationRecord.dto.OperationRecordForm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class OperationRecordFormMapper {

    // ISO-8601（例: 2026-01-01T10:00）対応
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * null/空/空白を弾いて文字列を返す（エラーを分かりやすくするため）
     */
    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is null/blank");
        }
        return value.trim();
    }

    /**
     * ISO_LOCAL_DATE_TIME でLocalDateTimeに変換。null/空は明示エラー。
     * parse 失敗時も DateTimeParseException をラップして何が悪いか分かるようにする。
     */
    private static LocalDateTime parseLocalDateTime(String value, String fieldName) {
        String v = requireText(value, fieldName);
        try {
            return LocalDateTime.parse(v, FORMATTER);
        } catch (DateTimeParseException ex) {
            // 形式不正は NPE ではなく「入力が不正」として返す
            throw new IllegalArgumentException(fieldName + " format is invalid: " + v, ex);
        }
    }

    /**
     * 全角→半角を行い、数値だけを許可して int にする。
     * 数値以外が混ざっていたら明示エラー。
     */
    private static int toIntNumber(String text, String fieldName) {
        String v = requireText(text, fieldName);

        StringBuilder sb = new StringBuilder(v.length());
        for (char c : v.toCharArray()) {
            if (c >= '０' && c <= '９') {
                sb.append((char) (c - '０' + '0'));
            } else {
                sb.append(c);
            }
        }

        String normalized = sb.toString().trim();

        // 数字以外が入っていたら弾く（必要なら許可ルールを変えられます）
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
     * Double変換。null/空は明示エラー。全角数字が来る場合も考慮するなら正規化を追加可。
     */
    private static double toDouble(String text, String fieldName) {
        String v = requireText(text, fieldName);

        // 全角数字が来る可能性があるなら、同じ変換を適用
        StringBuilder sb = new StringBuilder(v.length());
        for (char c : v.toCharArray()) {
            if (c >= '０' && c <= '９') {
                sb.append((char) (c - '０' + '0'));
            } else if (c == '．') { // 全角ドット対策（必要なら）
                sb.append('.');
            } else {
                sb.append(c);
            }
        }

        String normalized = sb.toString().trim();

        try {
            return Double.parseDouble(normalized);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " must be a decimal number: " + normalized, ex);
        }
    }

    public static OperationRecordRequest fromFormToRequest(OperationRecordForm form) {
        if (form == null) {
            throw new IllegalArgumentException("form is null");
        }

        OperationRecordRequest req = new OperationRecordRequest();

        // ここも null/空を弾いてから変換（途中入力で落ちないように明示エラー）
        req.setDriverId(Long.valueOf(requireText(form.getDriverId(), "driverId")));
        req.setVehicleId(Long.valueOf(requireText(form.getVehicleId(), "vehicleId")));

        // DatetimePicker（2026-01-01T10:00）に対応
        // parse対象がnullだとNPEになるので必ずガードする [1](https://www.geeksforgeeks.org/java/localdatetime-parse-method-in-java-with-examples/)[2](https://docs.oracle.com/javase/jp/8/docs/api/java/time/format/DateTimeFormatter.html)
        req.setStartDateTime(parseLocalDateTime(form.getStartDateTime(), "startDateTime"));
        req.setEndDateTime(parseLocalDateTime(form.getEndDateTime(), "endDateTime"));

        req.setStartMeter(toIntNumber(form.getStartMeter(), "startMeter"));
        req.setEndMeter(toIntNumber(form.getEndMeter(), "endMeter"));

        req.setFuelRate(toDouble(form.getFuelRate(), "fuelRate"));

        return req;
    }
}
