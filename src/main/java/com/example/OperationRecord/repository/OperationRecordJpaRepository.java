package com.example.OperationRecord.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.OperationRecord.entity.OperationRecordEntity;

public interface OperationRecordJpaRepository
        extends JpaRepository<OperationRecordEntity, Long> {
}
