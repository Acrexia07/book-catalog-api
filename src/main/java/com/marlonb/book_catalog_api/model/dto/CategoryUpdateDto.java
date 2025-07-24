package com.marlonb.book_catalog_api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateDto {

    @NotBlank(message = "Name is Required!")
    @Size(max = 50, message = "Name must not exceed 50 characters!")
    private String name;

    @NotBlank(message = "Slug is Required!")
    @Size(max = 50, message = "Slug must not exceed 50 characters!")
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
            message = "Slug must contain only lowercase letters, " +
                    "numbers, and hyphens (no leading / trailing or consecutive hyphens)")
    private String slug;

    @Size(max = 255, message = "Description must not exceed 255 characters!")
    private String description;

    @NotNull(message = "Active Status is Required!")
    private Boolean isActive;
}
