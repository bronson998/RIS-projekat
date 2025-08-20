package com.projekat.ris.dto.mappers;

import com.projekat.ris.dto.CategoryDTO;
import com.projekat.ris.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toDTO(Category category);
    Category toEntity(CategoryDTO categoryDTO);
}
