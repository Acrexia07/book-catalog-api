package com.marlonb.book_catalog_api.utils.test_assertions;

import com.marlonb.book_catalog_api.model.dto.BookRequestDto;
import com.marlonb.book_catalog_api.model.dto.BookResponseDto;
import com.marlonb.book_catalog_api.model.dto.BookUpdateDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.Set;

public class BookTestAssertions {

    // Assertion: Using Recursive Comparison
    public static void forComparingBookAssertions (BookResponseDto result,
                                               BookResponseDto expectedResponse) {
        Assertions.assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    public static void forComparingBookAssertions (List<BookResponseDto> result,
                                               List<BookResponseDto> expectedResponse) {
        Assertions.assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    public static void constraintViolationBookAssertions (BookRequestDto createBookRequest,
                                                      BookUpdateDto updateBook) {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<BookRequestDto>> requestViolations =
                validator.validate(createBookRequest);
        Set<ConstraintViolation<BookUpdateDto>> updateViolations =
                validator.validate(updateBook);

        // Assert
        Assertions.assertThat(requestViolations).isNotEmpty();
        Assertions.assertThat(updateViolations).isNotEmpty();
    }
}
