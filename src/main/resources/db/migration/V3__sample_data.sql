-- Sample Users
INSERT INTO tbl_app_users
(id, email, password_hash, user_role)
VALUES(1, 'aircraft.operator@example.com', '$2a$10$fndOQKHciBPiYOqNH1HcRetVaOQC9lSK276Mt6wy6DanDxn073I1y', 'AIRCRAFT_OPERATOR');
INSERT INTO tbl_app_users
(id, email, password_hash, user_role)
VALUES(2, 'operation.manager@example.com', '$2a$10$W9hViAifRdRbHN4dOlOJC.Hb6M7kiWLmYQI59SXs.L9z/26HaDKS6', 'OPERATIONS_MANAGER');


-- Sample Orders
INSERT INTO tbl_fuel_orders
(id, tail_number, airport_icao, requested_fuel_volume, delivery_window_start, delivery_window_end, status, created_at, updated_at, created_by, updated_by, is_active, is_deleted)
VALUES('9305a4db-a522-4f4b-a9b8-7c7c9bf8d1bf'::uuid, 'FB1272', 'OMDB', 2000.000, '2025-10-18 08:17:00.000', '2025-10-18 09:18:00.000', 'PENDING', '2025-10-17 12:19:47.154', '2025-10-17 12:19:47.154', 'aircraft.operator@example.com', 'user@example.com', true, false);
INSERT INTO tbl_fuel_orders
(id, tail_number, airport_icao, requested_fuel_volume, delivery_window_start, delivery_window_end, status, created_at, updated_at, created_by, updated_by, is_active, is_deleted)
VALUES('a1fbb4e7-9c1f-4d7c-a521-2bb7d2ad5c91'::uuid, 'FB101', 'OMDB', 1800.000, '2025-10-18 07:30:00.000', '2025-10-18 08:30:00.000', 'PENDING', '2025-10-17 12:20:01.123', '2025-10-17 12:20:01.123', 'aircraft.operator@example.com', 'user@example.com', true, false);
INSERT INTO tbl_fuel_orders
(id, tail_number, airport_icao, requested_fuel_volume, delivery_window_start, delivery_window_end, status, created_at, updated_at, created_by, updated_by, is_active, is_deleted)
VALUES('f20c49a2-92f8-43d3-8d9b-723a772dd9d0'::uuid, 'FB102', 'OMAA', 2500.000, '2025-10-18 09:00:00.000', '2025-10-18 10:15:00.000', 'PENDING', '2025-10-17 12:21:01.567', '2025-10-17 12:21:01.567', 'aircraft.operator@example.com', 'user@example.com', true, false);
INSERT INTO tbl_fuel_orders
(id, tail_number, airport_icao, requested_fuel_volume, delivery_window_start, delivery_window_end, status, created_at, updated_at, created_by, updated_by, is_active, is_deleted)
VALUES('374819f2-1d8d-4fd5-81d6-6ae12bf559d8'::uuid, 'FB127', 'OMDB', 2000.000, '2025-10-18 08:17:00.000', '2025-10-18 09:18:00.000', 'COMPLETED', '2025-10-17 12:18:09.291', '2025-10-17 12:27:09.074', 'aircraft.operator@example.com', 'operation.manager@example.com', true, false);
INSERT INTO tbl_fuel_orders
(id, tail_number, airport_icao, requested_fuel_volume, delivery_window_start, delivery_window_end, status, created_at, updated_at, created_by, updated_by, is_active, is_deleted)
VALUES('8beafdc4-8048-4a1d-92e4-cc67f4d94a34'::uuid, 'FB106', 'WSSS', 5000.000, '2025-10-18 08:30:00.000', '2025-10-18 09:40:00.000', 'CONFIRMED', '2025-10-17 12:23:01.499', '2025-10-17 12:33:48.671', 'aircraft.operator@example.com', 'operation.manager@example.com', true, false);
INSERT INTO tbl_fuel_orders
(id, tail_number, airport_icao, requested_fuel_volume, delivery_window_start, delivery_window_end, status, created_at, updated_at, created_by, updated_by, is_active, is_deleted)
VALUES('9f7e3e24-cc90-4c97-b65f-29b5d4df4fa3'::uuid, 'FB104', 'EGLL', 4000.000, '2025-10-18 05:00:00.000', '2025-10-18 06:10:00.000', 'CONFIRMED', '2025-10-17 12:22:01.231', '2025-10-17 13:41:21.031', 'aircraft.operator@example.com', 'operation.manager@example.com', true, false);