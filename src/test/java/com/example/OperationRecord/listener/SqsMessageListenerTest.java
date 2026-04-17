package com.example.OperationRecord.listener;

import static org.mockito.Mockito.*;

import com.example.OperationRecord.entity.OperationRecordEntity;
import com.example.OperationRecord.repository.OperationRecordJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class SqsMessageListenerTest {

    private final ObjectMapper mapper;
    private final OperationRecordJpaRepository repository = mock(OperationRecordJpaRepository.class);
    private final SqsMessageListener listener;

    public SqsMessageListenerTest() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // SqsClient は使わないので null を渡す
        listener = new SqsMessageListener(null, mapper, repository);
    }

    @Test
    void メッセージを受信してDBに保存できる() throws Exception {

        String json = """
        {
            "vehicleId": 1,
            "driverId": 2,
            "startDateTime": "2026-04-17T10:00:00",
            "endDateTime": "2026-04-17T12:00:00",
            "startMeter": 1000,
            "endMeter": 1200,
            "fuelRate": 12.5
        }
        """;

        listener.handleMessage(json);

        ArgumentCaptor<OperationRecordEntity> captor =
                ArgumentCaptor.forClass(OperationRecordEntity.class);

        verify(repository, times(1)).save(captor.capture());

        OperationRecordEntity saved = captor.getValue();

        assert saved.getVehicleId() == 1;
        assert saved.getDriverId() == 2;
        assert saved.getStartMeter() == 1000;
        assert saved.getEndMeter() == 1200;
    }
}
