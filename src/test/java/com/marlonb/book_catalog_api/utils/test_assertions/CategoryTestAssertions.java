package com.marlonb.book_catalog_api.utils.test_assertions;

import com.marlonb.book_catalog_api.model.dto.CategoryRequestDto;
import com.marlonb.book_catalog_api.model.dto.CategoryResponseDto;
import com.marlonb.book_catalog_api.model.dto.CategoryUpdateDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.Set;

public class CategoryTestAssertions {

    // Assertion: Using Recursive Comparison
    public static void forComparingCategoryAssertions (CategoryResponseDto result,
                                                      CategoryResponseDto expectedResponse) {
        Assertions.assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    public static void forComparingCategoryAssertions (List<CategoryResponseDto> result,
                                                      List<CategoryResponseDto> expectedResponse) {
        Assertions.assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    public static void constraintViolationCategoryAssertions (CategoryRequestDto createCategoryRequest,
                                                              CategoryUpdateDto updateCategory) {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<CategoryRequestDto>> requestViolations =
                validator.validate(createCategoryRequest);
        Set<ConstraintViolation<CategoryUpdateDto>> updateViolations =
                validator.validate(updateCategory);

        // Assert
        Assertions.assertThat(requestViolations).isNotEmpty();
        Assertions.assertThat(updateViolations).isNotEmpty();
    }
}
