package com.sandwich.app.mapper;

import com.sandwich.app.domain.dto.OrderDto;
import com.sandwich.app.domain.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface OrderMapper {

    OrderDto convert(OrderEntity entity);

    @Mapping(target = "id", ignore = true)
    OrderEntity convert(@MappingTarget OrderEntity entity, OrderDto dto);
}
