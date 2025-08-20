package com.projekat.ris.dto.mappers;

import com.projekat.ris.dto.WishlistItemDTO;
import com.projekat.ris.model.WishlistItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WishlistItemMapper {
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "productPrice")
    WishlistItemDTO toDto(WishlistItem item);

    @Mapping(source = "productId", target = "product.id")
    WishlistItem toEntity(WishlistItemDTO dto);
}
