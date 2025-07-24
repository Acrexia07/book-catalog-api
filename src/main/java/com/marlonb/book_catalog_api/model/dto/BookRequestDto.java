package com.marlonb.book_catalog_api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookRequestDto {

    @NotBlank(message = "Title is Required!")
    @Size(max = 255, message = "Title must not exceed 255 characters!")
    private String title;

    @NotBlank(message = "Author is Required!")
    @Size(max = 50, message = "Author must not exceed 50 characters!")
    private String author;

    @NotNull(message = "Category id is Required!")
    private Long categoryId;

    @NotNull(message = "Pages is Required!")
    @Positive(message = "Pages must be a positive number")
    private int pages;

    @NotNull(message = "Stock Status (isInStock) is Required!")
    private Boolean isInStock;
}
