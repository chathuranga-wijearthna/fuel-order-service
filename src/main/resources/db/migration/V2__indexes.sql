-- V2__indexes.sql: helpful indexes
CREATE INDEX IF NOT EXISTS idx_fuel_orders_airport_icao ON tbl_fuel_orders(airport_icao);
CREATE INDEX IF NOT EXISTS idx_fuel_orders_created_at ON tbl_fuel_orders(created_at);
