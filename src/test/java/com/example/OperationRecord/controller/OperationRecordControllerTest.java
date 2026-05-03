package com.example.OperationRecord.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.OperationRecord.domain.OperationRecord;
import com.example.OperationRecord.service.application.OperationRecordService;

class OperationRecordControllerTest {

    private MockMvc mockMvc;
    private OperationRecordService service;

    @BeforeEach
    void setup() {
        service = Mockito.mock(OperationRecordService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new OperationRecordController(service))
                .build();
    }

    @Test
    void POSTで新規登録できる() throws Exception {

        when(service.regist(any())).thenReturn(
                new OperationRecord(
                        1L,
                        1L,
                        10L,
                        LocalDateTime.of(2026, 4, 15, 9, 0),
                        12000
                )
        );

        mockMvc.perform(post("/operation-records")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "vehicleId": 1,
                        "driverId": 10,
                        "startDateTime": "2026-04-15T09:00:00",
                        "startMeter": 12000
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(service).regist(any());
    }

    @Test
    void GETでID指定取得できる() throws Exception {

        when(service.findById(1L)).thenReturn(
                new OperationRecord(
                        1L,
                        1L,
                        10L,
                        LocalDateTime.of(2026, 4, 15, 9, 0),
                        12000
                )
        );

        mockMvc.perform(get("/operation-records/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(service).findById(1L);
    }

    @Test
    void GETで全件取得できる() throws Exception {

        when(service.findAll()).thenReturn(List.of(
                new OperationRecord(
                        1L, 1L, 10L,
                        LocalDateTime.of(2026, 4, 15, 9, 0),
                        12000
                ),
                new OperationRecord(
                        2L, 2L, 20L,
                        LocalDateTime.of(2026, 4, 16, 9, 0),
                        13000
                )
        ));

        mockMvc.perform(get("/operation-records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(service).findAll();
    }

    @Test
    void DELETEで削除できる() throws Exception {

        mockMvc.perform(delete("/operation-records/1"))
                .andExpect(status().isOk());

        verify(service).remove(1L);
    }
}