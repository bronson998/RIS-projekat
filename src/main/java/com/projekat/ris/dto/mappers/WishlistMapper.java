package com.projekat.ris.dto.mappers;

import com.projekat.ris.dto.WishlistDTO;
import com.projekat.ris.model.Wishlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {WishlistItemMapper.class})
public interface WishlistMapper {
    @Mapping(source = "user.id", target = "userId")
    WishlistDTO toDto(Wishlist wishlist);

    @Mapping(source = "userId", target = "user.id")
    Wishlist toEntity(WishlistDTO wishlistDTO);
}
