package com.example.OperationRecord.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class OperationRecordFormTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testEndMeter_半角数字_OK() {
        OperationRecordForm form = new OperationRecordForm();
        form.setEndMeter("12345");

        Set<ConstraintViolation<OperationRecordForm>> violations = validator.validateProperty(form, "endMeter");
        assertTrue(violations.isEmpty());
    }

    @Test
    void testEndMeter_全角数字_OK() {
        OperationRecordForm form = new OperationRecordForm();
        form.setEndMeter("１２３４５");

        Set<ConstraintViolation<OperationRecordForm>> violations = validator.validateProperty(form, "endMeter");
        assertTrue(violations.isEmpty());
    }

    @Test
    void testEndMeter_数字以外_NG() {
        OperationRecordForm form = new OperationRecordForm();
        form.setEndMeter("12km");

        Set<ConstraintViolation<OperationRecordForm>> violations = validator.validateProperty(form, "endMeter");
        assertFalse(violations.isEmpty());
    }
}

