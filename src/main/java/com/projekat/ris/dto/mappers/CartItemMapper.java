package com.projekat.ris.dto.mappers;

import com.projekat.ris.dto.CartItemDTO;
import com.projekat.ris.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(source="product.id", target="productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "price")
    CartItemDTO toDTO(CartItem cartItem);

    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "productName", target = "product.name")
    @Mapping(target = "product.price", ignore = true)
    CartItem toEntity(CartItemDTO cartItemDTO);
}
