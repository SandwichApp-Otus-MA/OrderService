--liquibase formatted sql
--changeset AVoronov:v1.0.0/orders

CREATE TABLE IF NOT EXISTS orders
(
    id uuid NOT NULL PRIMARY KEY,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    user_id uuid NOT NULL,
    position_ids uuid[] NOT NULL
);