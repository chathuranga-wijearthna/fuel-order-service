CREATE TABLE IF NOT EXISTS tbl_app_users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    user_role VARCHAR(255) NOT NULL);

CREATE TABLE IF NOT EXISTS tbl_fuel_orders (
    id UUID PRIMARY KEY,
    tail_number VARCHAR(255) NOT NULL,
    airport_icao VARCHAR(4) NOT NULL,
    requested_fuel_volume NUMERIC(18,3) NOT NULL,
    delivery_window_start TIMESTAMP NOT NULL,
    delivery_window_end TIMESTAMP NOT NULL,
    status VARCHAR(16) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255) NULL,
    is_active BOOLEAN NOT NULL,
    is_deleted BOOLEAN NOT NULL);
