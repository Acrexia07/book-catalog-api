package com.marlonb.book_catalog_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BookResponseDto {

    private long id;

    private String title;

    private String author;

    private CategoryResponseDto category;

    private int pages;

    private Boolean isInStock;
}
