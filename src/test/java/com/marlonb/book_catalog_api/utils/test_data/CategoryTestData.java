package com.marlonb.book_catalog_api.utils.test_data;

import com.marlonb.book_catalog_api.model.CategoryEntity;
import com.marlonb.book_catalog_api.model.dto.CategoryRequestDto;
import com.marlonb.book_catalog_api.model.dto.CategoryResponseDto;
import com.marlonb.book_catalog_api.model.dto.CategoryUpdateDto;

public class CategoryTestData {

    public static CategoryEntity sampleCategoryEntity () {

        var sampleCategory = new CategoryEntity();
        sampleCategory.setId(1L);
        sampleCategory.setName("Fantasy");
        sampleCategory.setSlug("fantasy");
        sampleCategory.setDescription("Genre about fictional elements in the story");
        sampleCategory.setIsActive(true);

        return sampleCategory;
    }

    public static CategoryEntity sampleNewCategoryEntity () {

        var sampleNewCategory = new CategoryEntity();
        sampleNewCategory.setId(2L);
        sampleNewCategory.setName("Horror");
        sampleNewCategory.setSlug("horror");
        sampleNewCategory.setDescription("Genre about scary things");
        sampleNewCategory.setIsActive(true);

        return sampleNewCategory;
    }

    public static CategoryRequestDto sampleCategoryRequest () {

        return new CategoryRequestDto(
                sampleCategoryEntity().getName(),
                sampleCategoryEntity().getSlug(),
                sampleCategoryEntity().getDescription(),
                sampleCategoryEntity().getIsActive()
        );
    }

    public static CategoryResponseDto sampleCategoryResponse () {

        return new CategoryResponseDto(
                sampleCategoryEntity().getId(),
                sampleCategoryEntity().getName(),
                sampleCategoryEntity().getSlug(),
                sampleCategoryEntity().getDescription(),
                sampleCategoryEntity().getIsActive()
        );
    }

    public static CategoryResponseDto sampleNewCategoryResponse () {

        return new CategoryResponseDto(
                sampleNewCategoryEntity().getId(),
                sampleNewCategoryEntity().getName(),
                sampleNewCategoryEntity().getSlug(),
                sampleNewCategoryEntity().getDescription(),
                sampleNewCategoryEntity().getIsActive()
        );
    }

    public static CategoryUpdateDto sampleCategoryUpdate () {
        return new CategoryUpdateDto(
                sampleNewCategoryEntity().getName(),
                sampleNewCategoryEntity().getSlug(),
                sampleNewCategoryEntity().getDescription(),
                sampleNewCategoryEntity().getIsActive()
        );
    }
}
