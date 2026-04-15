package com.example.OperationRecord.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.OperationRecord.entity.OperationRecordEntity;

class InMemoryOperationRecordRepositoryTest {

    private InMemoryOperationRecordRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOperationRecordRepository();
        repository.clear(); // テストごとに初期化
    }

    @Test
    void 保存したエンティティを取得できる() {

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

        OperationRecordEntity saved = repository.insert(entity);

        OperationRecordEntity found = repository.selectById(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
    }

    @Test
    void 全件取得できる() {

        repository.insert(new OperationRecordEntity(
                null, 1L, 10L,
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                12000, 12100, 165.5
        ));

        repository.insert(new OperationRecordEntity(
                null, 2L, 20L,
                LocalDate.now(),
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                5000, 5200, 150.0
        ));

        assertEquals(2, repository.selectAll().size());
    }

    @Test
    void 削除したエンティティは取得できない() {

        OperationRecordEntity saved = repository.insert(new OperationRecordEntity(
                null, 1L, 10L,
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                12000, 12100, 165.5
        ));

        repository.deleteById(saved.getId());

        assertNull(repository.selectById(saved.getId()));
    }

    @Test
    void clearで全件削除される() {

        repository.insert(new OperationRecordEntity(
                null, 1L, 10L,
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                12000, 12100, 165.5
        ));

        repository.insert(new OperationRecordEntity(
                null, 2L, 20L,
                LocalDate.now(),
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                5000, 5200, 150.0
        ));

        repository.clear();

        assertEquals(0, repository.selectAll().size());
    }
}
