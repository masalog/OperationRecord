package com.example.OperationRecord.service.operationRecord;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import com.example.OperationRecord.domain.OperationRecord;
import com.example.OperationRecord.entity.OperationRecordEntity;
import com.example.OperationRecord.repository.OperationRecordJpaRepository;

@ExtendWith(MockitoExtension.class)
class OperationRecordServiceImplTest {

    @Mock
    private OperationRecordJpaRepository repository;

    @InjectMocks
    private OperationRecordServiceImpl service;

    @Test
    void 登録したデータを取得できる() {

        OperationRecord domain = new OperationRecord(
                null, 1L, 10L,
                LocalDateTime.of(2026, 4, 15, 9, 0),
                LocalDateTime.of(2026, 4, 15, 18, 0),
                12000, 12100, 165.5
        );

        // ★ setId() を使わず、コンストラクタで id をセットする
        OperationRecordEntity entity = new OperationRecordEntity(
                1L,                      // ← id
                1L,
                10L,
                LocalDateTime.of(2026, 4, 15, 9, 0),
                LocalDateTime.of(2026, 4, 15, 18, 0),
                12000,
                12100,
                165.5
        );

        when(repository.save(any())).thenReturn(entity);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        OperationRecord saved = service.regist(domain);
        assertNotNull(saved);
        assertEquals(1L, saved.getId());

        OperationRecord found = service.findById(1L);
        assertNotNull(found);
        assertEquals(1L, found.getId());
    }
}
