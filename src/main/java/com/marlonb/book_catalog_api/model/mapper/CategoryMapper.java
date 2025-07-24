package com.marlonb.book_catalog_api.model.mapper;

import com.marlonb.book_catalog_api.model.CategoryEntity;
import com.marlonb.book_catalog_api.model.dto.CategoryRequestDto;
import com.marlonb.book_catalog_api.model.dto.CategoryResponseDto;
import com.marlonb.book_catalog_api.model.dto.CategoryUpdateDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface CategoryMapper {

    // CategoryRequestDto → CategoryEntity (for create operation)
    @Mapping(target = "id", ignore = true)
    CategoryEntity toEntity (CategoryRequestDto createCategoryRequest);

    // CategoryEntity → CategoryResponseDto (for read operation)
    CategoryResponseDto toResponse (CategoryEntity category);

    // Void — Update Mapping (for update operation)
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity (CategoryUpdateDto dto, @MappingTarget CategoryEntity entity);

    // mapping category id → category entity
    default CategoryEntity fromId (Long id){
        if (id == null) return null;
        CategoryEntity category = new CategoryEntity();
        category.setId(id);
        return category;
    }
}
