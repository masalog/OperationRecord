package com.example.OperationRecord.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.OperationRecord.entity.OperationRecordEntity;
import com.example.OperationRecord.repository.InMemoryOperationRecordRepository;

class OperationRecordServiceImplTest {

    private OperationRecordService service;
    private InMemoryOperationRecordRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOperationRecordRepository();
        repository.clear();
        service = new OperationRecordServiceImpl(repository);
    }

    @Test
    void 登録したデータを取得できる() {

        OperationRecordEntity entity = new OperationRecordEntity(
                null,
                1L,
                10L,
                LocalDate.of(2026, 4, 15),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                12000,
                12100,
                165.5
        );

        OperationRecordEntity saved = service.register(entity);

        OperationRecordEntity found = service.find(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
    }

    @Test
    void 全件取得できる() {

        service.register(new OperationRecordEntity(
                null, 1L, 10L,
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                12000, 12100, 165.5
        ));

        service.register(new OperationRecordEntity(
                null, 2L, 20L,
                LocalDate.now(),
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                5000, 5200, 150.0
        ));

        assertEquals(2, service.findAll().size());
    }

    @Test
    void 削除したデータは取得できない() {

        OperationRecordEntity saved = service.register(new OperationRecordEntity(
                null, 1L, 10L,
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                12000, 12100, 165.5
        ));

        service.delete(saved.getId());

        assertNull(service.find(saved.getId()));
    }
}