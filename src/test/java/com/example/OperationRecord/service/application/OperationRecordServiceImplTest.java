package com.example.OperationRecord.service.application;

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
import com.example.OperationRecord.exception.ResourceNotFoundException;
import com.example.OperationRecord.repository.OperationRecordJpaRepository;

@ExtendWith(MockitoExtension.class)
class OperationRecordServiceImplTest {

    @Mock
    private OperationRecordJpaRepository repository;

    @InjectMocks
    private OperationRecordServiceImpl service;

    @Test
    void 新規登録後に運行記録の番号が確定する() {

        // 前半登録用のドメイン
        OperationRecord domain = new OperationRecord(
                null,
                1L,
                10L,
                LocalDateTime.of(2026, 4, 15, 9, 0),
                12000
        );

        // 保存後に番号が付与されたエンティティ
        OperationRecordEntity savedEntity = new OperationRecordEntity(
                1L,
                1L,
                10L,
                LocalDateTime.of(2026, 4, 15, 9, 0),
                null,
                12000,
                null
        );

        when(repository.save(any())).thenReturn(savedEntity);
        when(repository.findById(1L)).thenReturn(Optional.of(savedEntity));

        // 登録
        OperationRecord saved = service.regist(domain);
        assertNotNull(saved);
        assertEquals(1L, saved.getId());

        // 取得できることも確認
        OperationRecord found = service.findById(1L);
        assertNotNull(found);
        assertEquals(1L, found.getId());
        assertNull(found.getEndDateTime());
        assertNull(found.getEndMeter());
    }

    @Test
    void 後半更新で終了日時と終了メーターが反映される() {

        // save 後のエンティティ（後半更新後）
        OperationRecordEntity updatedEntity = new OperationRecordEntity(
                1L,
                1L,
                10L,
                LocalDateTime.of(2026, 4, 15, 9, 0),
                LocalDateTime.of(2026, 4, 15, 18, 0),
                12000,
                12100
        );

        when(repository.save(any())).thenReturn(updatedEntity);

        // ドメイン（前半）
        OperationRecord domain = new OperationRecord(
                1L,
                1L,
                10L,
                LocalDateTime.of(2026, 4, 15, 9, 0),
                12000
        );

        // 後半情報をセット
        domain.updateEndInfo(
                LocalDateTime.of(2026, 4, 15, 18, 0),
                12100
        );

        // 更新実行
        OperationRecord updated = service.update(domain);

        assertEquals(
                LocalDateTime.of(2026, 4, 15, 18, 0),
                updated.getEndDateTime()
        );
        assertEquals(12100, updated.getEndMeter());
    }
}

