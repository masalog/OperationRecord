package com.example.OperationRecord.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import com.example.OperationRecord.service.operationRecord.OperationRecordService;

class OperationRecordControllerTest {

    private MockMvc mockMvc;
    private OperationRecordService service;

    @BeforeEach
    void setup() {
        service = Mockito.mock(OperationRecordService.class);

        OperationRecordController controller = new OperationRecordController(service);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

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

        when(service.findById(anyLong())).thenReturn(domain);

        mockMvc.perform(get("/operation-records/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.vehicleId").value(1))
                .andExpect(jsonPath("$.driverId").value(10))
                .andExpect(jsonPath("$.startDateTime").value("2026-04-15T09:00:00"))
                .andExpect(jsonPath("$.endDateTime").value("2026-04-15T18:00:00"));
    }

    @Test
    void GETで全件取得できる() throws Exception {

        OperationRecord record1 = new OperationRecord(
                1L, 1L, 10L,
                LocalDateTime.of(2026, 4, 15, 9, 0),
                LocalDateTime.of(2026, 4, 15, 18, 0),
                12000, 12100, 165.5
        );

        OperationRecord record2 = new OperationRecord(
                2L, 2L, 20L,
                LocalDateTime.of(2026, 4, 16, 9, 0),
                LocalDateTime.of(2026, 4, 16, 18, 0),
                13000, 13100, 170.0
        );

        when(service.findAll()).thenReturn(List.of(record1, record2));

        mockMvc.perform(get("/operation-records")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void DELETEで削除できる() throws Exception {

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .delete("/operation-records/1")
        )
        .andExpect(status().isOk());

        Mockito.verify(service).remove(1L);
    }

}
