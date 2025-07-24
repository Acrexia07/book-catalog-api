package com.marlonb.book_catalog_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CategoryResponseDto {

    private long id;

    private String name;

    private String slug;

    private String description;

    private Boolean isActive;
}
