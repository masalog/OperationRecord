package com.example.OperationRecord.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.OperationRecord.domain.OperationRecord;
import com.example.OperationRecord.dto.OperationRecordRequest;
import com.example.OperationRecord.dto.OperationRecordResponse;
import com.example.OperationRecord.mapper.OperationRecordMapper;
import com.example.OperationRecord.service.operationRecord.OperationRecordService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/operation-records")
@RequiredArgsConstructor
public class OperationRecordController {

    private final OperationRecordService service;

    /** 新規登録 */
    @PostMapping
    public OperationRecordResponse create(@RequestBody OperationRecordRequest request) {

        // DTO → Domain
        OperationRecord domain = OperationRecordMapper.fromRequestToDomain(request);

        // Service に渡して保存
        OperationRecord savedDomain = service.regist(domain);

        // Domain → ResponseDTO
        return OperationRecordMapper.fromDomainToResponse(savedDomain);
    }

    /** ID で取得 */
    @GetMapping("/{id}")
    public OperationRecordResponse getById(@PathVariable Long id) {

        OperationRecord domain = service.findById(id);

        return OperationRecordMapper.fromDomainToResponse(domain);
    }

    /** 全件取得 */
    @GetMapping
    public List<OperationRecordResponse> getAll() {
        return service.findAll().stream()
                .map(OperationRecordMapper::fromDomainToResponse)
                .toList();
    }

    /** 削除 */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.remove(id);
    }

}
