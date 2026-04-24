CREATE TABLE IF NOT EXISTS operation_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id BIGINT NOT NULL,
    driver_id BIGINT NOT NULL,
    start_date_time DATETIME NOT NULL,
    end_date_time DATETIME NOT NULL,
    start_meter INT NOT NULL,
    end_meter INT NOT NULL,
    fuel_rate DOUBLE NOT NULL
);