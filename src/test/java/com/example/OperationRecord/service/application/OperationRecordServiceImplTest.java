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
import com.example.OperationRecord.mapper.OperationRecordMapper;
import com.example.OperationRecord.repository.OperationRecordJpaRepository;
import com.example.OperationRecord.service.application.OperationRecordServiceImpl;

@ExtendWith(MockitoExtension.class)
class OperationRecordServiceImplTest {

    @Mock
    private OperationRecordJpaRepository repository;

    @InjectMocks
    private OperationRecordServiceImpl service;

    @Test
    void 前半保存したデータを取得できる() {

        // 前半保存（終了情報は null）
        OperationRecord domain = new OperationRecord(
                null,
                1L,
                10L,
                LocalDateTime.of(2026, 4, 15, 9, 0),
                null,      // endDateTime
                12000,
                null,      // endMeter
                null       // fuelRate
        );

        // DB に保存された後の Entity（ID が付与される）
        OperationRecordEntity entity = new OperationRecordEntity(
                1L,
                1L,
                10L,
                LocalDateTime.of(2026, 4, 15, 9, 0),
                null,
                12000,
                null,
                null
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

        @Test
        void 後半更新が正しく動く() {

        // 前半保存済みのレコード（Entity）
        OperationRecordEntity existing = new OperationRecordEntity(
                1L,
                1L,
                10L,
                LocalDateTime.of(2026, 4, 15, 9, 0),
                null,
                12000,
                null,
                null
        );

        // Domain に変換
        OperationRecord domain = OperationRecordMapper.fromEntityToDomain(existing);

        // 後半情報をセット
        domain.updateEndInfo(
                LocalDateTime.of(2026, 4, 15, 18, 0),
                12100,
                165.5
        );

        // 更新後の Entity（save が返す値）
        OperationRecordEntity updatedEntity = new OperationRecordEntity(
                1L,
                1L,
                10L,
                LocalDateTime.of(2026, 4, 15, 9, 0),
                LocalDateTime.of(2026, 4, 15, 18, 0),
                12000,
                12100,
                165.5
        );

        // ★ findById のスタブは削除
        when(repository.save(any())).thenReturn(updatedEntity);

        OperationRecord updated = service.update(domain);

        assertEquals(12100, updated.getEndMeter());
        assertEquals(165.5, updated.getFuelRate());
        }

}
