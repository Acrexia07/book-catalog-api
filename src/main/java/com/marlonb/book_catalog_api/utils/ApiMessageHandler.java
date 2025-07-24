package com.marlonb.book_catalog_api.utils;

import com.marlonb.book_catalog_api.model.dto.CategoryResponseDto;

import java.util.List;

// Provides overloaded methods for generating standardized API responses
// Returns ApiResponseDto for a single CategoryResponseDto or a list of them
public class ApiMessageHandler {
    public static ApiResponseDto<CategoryResponseDto> getApiMessage
                        (String message, CategoryResponseDto categoryResponse) {
        return new ApiResponseDto<>(message, categoryResponse);
    }

    public static ApiResponseDto<List<CategoryResponseDto>> getApiMessage
                        (String message, List<CategoryResponseDto> categoryResponse) {
        return new ApiResponseDto<>(message, categoryResponse);
    }
}
