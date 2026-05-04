package com.example.OperationRecord.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineInputDto {
    private String userId;
    private String text;
    private String replyToken;
    private String rawBody;
    private String postbackDatetime;
}
