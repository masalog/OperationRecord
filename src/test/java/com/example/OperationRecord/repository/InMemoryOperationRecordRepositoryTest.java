package com.example.OperationRecord.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.OperationRecord.domain.OperationRecord;

class InMemoryOperationRecordRepositoryTest {

    private InMemoryOperationRecordRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOperationRecordRepository();
        repository.clear();
    }

    @Test
    void 保存したドメインを取得できる() {

        OperationRecord domain = new OperationRecord(
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

        OperationRecord saved = repository.save(domain);

        OperationRecord found = repository.findById(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
    }

    @Test
    void 全件取得できる() {

        repository.save(new OperationRecord(
                null, 1L, 10L,
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                12000, 12100, 165.5
        ));

        repository.save(new OperationRecord(
                null, 2L, 20L,
                LocalDate.now(),
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                5000, 5200, 150.0
        ));

        assertEquals(2, repository.findAll().size());
    }

    @Test
    void 削除したドメインは取得できない() {

        OperationRecord saved = repository.save(new OperationRecord(
                null, 1L, 10L,
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                12000, 12100, 165.5
        ));

        repository.remove(saved.getId());

        assertNull(repository.findById(saved.getId()));
    }

    @Test
    void clearで全件削除される() {

        repository.save(new OperationRecord(
                null, 1L, 10L,
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                12000, 12100, 165.5
        ));

        repository.save(new OperationRecord(
                null, 2L, 20L,
                LocalDate.now(),
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                5000, 5200, 150.0
        ));

        repository.clear();

        assertEquals(0, repository.findAll().size());
    }
}