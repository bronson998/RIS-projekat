package com.projekat.ris.dto.mappers;

import com.projekat.ris.dto.OrderItemDTO;
import com.projekat.ris.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(target = "productId", source = "product.id")
    OrderItemDTO toDto(OrderItem orderItem);

    @Mapping(target = "product.id", source = "productId")
    OrderItem toEntity(OrderItemDTO orderItemDTO);
}
