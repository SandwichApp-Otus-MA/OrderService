package com.sandwich.app.mapper;

import com.sandwich.app.domain.entity.OrderEntity;
import com.sandwich.app.domain.entity.OrderItemEntity;
import com.sandwich.app.models.model.order.OrderDto;
import com.sandwich.app.models.model.order.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper
public interface OrderMapper {

    OrderDto convert(OrderEntity entity);

    @Mapping(target = "id", ignore = true)
    OrderEntity convert(@MappingTarget OrderEntity entity, OrderDto dto);

    List<OrderItem> convert(List<OrderItemEntity> positions);
}
