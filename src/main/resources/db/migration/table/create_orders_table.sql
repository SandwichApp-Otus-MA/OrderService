--liquibase formatted sql
--changeset AVoronov:v1.0.0/orders

create table if not exists orders
(
    id uuid not null primary key,
    is_deleted boolean,
    deleted_at timestamp without time zone,
    user_id uuid not null,
    payment_id uuid,
    restaurant_id uuid,
    restaurant_comment text,
    delivery_id uuid,
    delivery_address text,
    delivery_comment text,
    amount decimal,
    status varchar(64)
);