package com.projekat.ris.dto.mappers;

import com.projekat.ris.dto.CartDTO;
import com.projekat.ris.model.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class})
public interface CartMapper {
    @Mapping(target = "userId", source = "user.id")
    CartDTO toDto(Cart cart);

    @Mapping(target = "user.id", source = "userId")
    Cart toEntity(CartDTO cartDTO);
}
