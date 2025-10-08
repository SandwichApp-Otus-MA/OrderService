package com.sandwich.app.mapper;

import com.sandwich.app.domain.entity.OrderEntity;
import com.sandwich.app.models.model.order.OrderDto;
import com.sandwich.app.models.model.order.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface OrderMapper {

    OrderDto convert(OrderEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "positionIds", source = "positions", qualifiedByName = "getPositionIds")
    OrderEntity convert(@MappingTarget OrderEntity entity, OrderDto dto);

    @Named("getPositionIds")
    default List<UUID> getPositionIds(List<OrderItem> positions) {
        return Optional.ofNullable(positions).orElse(List.of())
            .stream().map(OrderItem::getId).toList();
    }
}
