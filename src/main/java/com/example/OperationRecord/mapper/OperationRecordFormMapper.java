package com.example.OperationRecord.mapper;

import com.example.OperationRecord.dto.OperationRecordRequest;
import com.example.OperationRecord.dto.OperationRecordForm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OperationRecordFormMapper {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // 全角 → 半角変換して数値化
    private static int toNumber(String text) {
        StringBuilder sb = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (c >= '０' && c <= '９') {
                sb.append((char)(c - '０' + '0'));
            } else {
                sb.append(c);
            }
        }

        return Integer.parseInt(sb.toString());
    }


    public static OperationRecordRequest fromFormToRequest(OperationRecordForm form) {

        OperationRecordRequest req = new OperationRecordRequest();

        req.setDriverId(Long.valueOf(form.getDriverId()));
        req.setVehicleId(Long.valueOf(form.getVehicleId()));

        req.setStartDateTime(LocalDateTime.parse(form.getStartDateTime(), FORMATTER));
        req.setEndDateTime(LocalDateTime.parse(form.getEndDateTime(), FORMATTER));

        req.setStartMeter(toNumber(form.getStartMeter()));
        req.setEndMeter(toNumber(form.getEndMeter()));

        req.setFuelRate(Double.valueOf(form.getFuelRate()));

        return req;
    }
}

