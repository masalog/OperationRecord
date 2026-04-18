package com.example.OperationRecord.dto;

import lombok.Data;

@Data
public class LineInputDto {
    private String userId;
    private String text;
    private String replyToken;
    private String rawBody;
    private String postbackDatetime;
}
