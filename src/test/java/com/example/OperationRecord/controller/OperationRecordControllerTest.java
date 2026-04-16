package com.example.OperationRecord.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.OperationRecord.domain.OperationRecord;
import com.example.OperationRecord.service.OperationRecordService;

@WebMvcTest(OperationRecordController.class)
class OperationRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OperationRecordService service;

    @Test
    void POSTで新規登録できる() throws Exception {

        var requestJson = """
            {
                "vehicleId": 1,
                "driverId": 10,
                "startDateTime": "2026-04-15T09:00:00",
                "endDateTime": "2026-04-15T18:00:00",
                "startMeter": 12000,
                "endMeter": 12100,
                "fuelRate": 165.5
            }
            """;

        OperationRecord savedDomain = new OperationRecord(
                1L, 1L, 10L,
                LocalDateTime.of(2026, 4, 15, 9, 0),
                LocalDateTime.of(2026, 4, 15, 18, 0),
                12000, 12100, 165.5
        );

        when(service.regist(any())).thenReturn(savedDomain);

        mockMvc.perform(post("/operation-records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.vehicleId").value(1))
                .andExpect(jsonPath("$.driverId").value(10))
                .andExpect(jsonPath("$.startDateTime").value("2026-04-15T09:00:00"))
                .andExpect(jsonPath("$.endDateTime").value("2026-04-15T18:00:00"));
    }

    @Test
    void GETでID指定取得できる() throws Exception {

        OperationRecord domain = new OperationRecord(
                1L, 1L, 10L,
                LocalDateTime.of(2026, 4, 15, 9, 0),
                LocalDateTime.of(2026, 4, 15, 18, 0),
                12000, 12100, 165.5
        );

        when(service.findById(1L)).thenReturn(domain);

        mockMvc.perform(get("/operation-records/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.vehicleId").value(1))
                .andExpect(jsonPath("$.driverId").value(10))
                .andExpect(jsonPath("$.startDateTime").value("2026-04-15T09:00:00"))
                .andExpect(jsonPath("$.endDateTime").value("2026-04-15T18:00:00"));
    }

}