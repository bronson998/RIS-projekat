package com.projekat.ris.dto.mappers;

import com.projekat.ris.dto.OrderDTO;
import com.projekat.ris.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    OrderDTO toDto(Order order);

    @Mapping(target = "user.id", source = "userId")
    Order toEntity(OrderDTO orderDTO);
}
