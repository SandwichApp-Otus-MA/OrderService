--liquibase formatted sql
--changeset AVoronov:v1.0.0/order_items

create table order_items
(
    id uuid not null primary key,
    position_id uuid,
    order_id uuid
);

alter table order_items add constraint fk_order_items_on_order foreign key (order_id)
    references orders (id);