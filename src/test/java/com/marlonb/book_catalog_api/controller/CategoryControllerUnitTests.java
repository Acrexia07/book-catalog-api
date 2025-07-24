package com.marlonb.book_catalog_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marlonb.book_catalog_api.model.CategoryEntity;
import com.marlonb.book_catalog_api.model.dto.CategoryRequestDto;
import com.marlonb.book_catalog_api.model.dto.CategoryResponseDto;
import com.marlonb.book_catalog_api.model.dto.CategoryUpdateDto;
import com.marlonb.book_catalog_api.service.CategoryService;
import com.marlonb.book_catalog_api.exception.custom.DuplicateResourceFoundException;
import com.marlonb.book_catalog_api.exception.custom.ResourceNotFoundException;
import com.marlonb.book_catalog_api.utils.test_data.CategoryTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CategoryController.class)
public class CategoryControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    private CategoryEntity testCategory;
    private CategoryRequestDto categoryRequestDto;
    private CategoryResponseDto categoryResponseDto;
    private CategoryUpdateDto categoryUpdateDto;

    @TestConfiguration
    static class TestConfig {

        @Bean
        public CategoryService categoryService () {
            return mock(CategoryService.class);
        }
    }

    @BeforeEach
    void initSetup () {

        // Arrange initialization
        testCategory = CategoryTestData.sampleCategoryEntity();
        categoryRequestDto = CategoryTestData.sampleCategoryRequest();
        categoryResponseDto = CategoryTestData.sampleCategoryResponse();
        categoryUpdateDto = CategoryTestData.sampleCategoryUpdate();

        // Reset specific mock
        reset(categoryService);
    }

    @Nested
    class PositiveTests {

        @Test
        @DisplayName("UTP-CC-C001: Should return response entity when create new category")
        void shouldReturnResponseEntityWhenCreateNewCategory () throws Exception {

            // Stub
            when(categoryService.saveCategoryData(any())).thenReturn(categoryResponseDto);

            String categoryJsonRequest = new ObjectMapper().writeValueAsString(categoryRequestDto);

            // Act + Assert
            mockMvc.perform(post("/api/categories")
                        .with(csrf())
                        .with(httpBasic("acrexia", "dummy"))
                        .contentType("application/json")
                        .content(categoryJsonRequest))
                    .andExpectAll(
                        status().isCreated(),
                        header().exists("Location"),
                        header().string("Location", containsString("/api/categories/")),
                        jsonPath("$.message").value("Category created successfully!"),
                        jsonPath("$.data.name").value(categoryRequestDto.getName()));
        }

        @Test
        @DisplayName("UTP-CC-R001: Should return response entity when retrieve list of categories")
        void shouldReturnResponseEntityWhenRetrieveListOfCategories () throws Exception {

            // Arrange
            CategoryResponseDto categoryResponse1 = CategoryTestData.sampleCategoryResponse();
            CategoryResponseDto categoryResponse2 = CategoryTestData.sampleNewCategoryResponse();

            List<CategoryResponseDto> listOfCategoryResponse =
                    List.of(categoryResponse1, categoryResponse2);

            // Stub
            when(categoryService.getAllCategories()).thenReturn(listOfCategoryResponse);

            String categoryJsonResponse = new ObjectMapper().writeValueAsString(listOfCategoryResponse);

            // Act + Assert
            mockMvc.perform(get("/api/categories")
                        .with(httpBasic("acrexia", "dummy"))
                        .contentType("application/json")
                        .content(categoryJsonResponse))
                    .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message")
                                .value("Categories retrieved successfully!"),
                        jsonPath("$.data").isArray(),
                        jsonPath("$.data[*].name",
                                hasItems(categoryResponse1.getName(), categoryResponse2.getName())),
                        jsonPath("$.data.length()").value(2));
        }

        @Test
        @DisplayName("UTP-CC-R002: Should return response entity when retrieve specific category")
        void shouldReturnResponseEntityWhenRetrieveSpecificCategory () throws Exception {

            // Arrange
            final long categoryResponseId = categoryResponseDto.getId();

            // Stub
            when(categoryService.getCategoryById(categoryResponseId))
                    .thenReturn(categoryResponseDto);

            String categoryJsonResponse = new ObjectMapper().writeValueAsString(categoryResponseDto);

            mockMvc.perform(get("/api/categories/{id}", categoryResponseId)
                        .with(httpBasic("acrexia", "dummy"))
                        .contentType("application/json")
                        .content(categoryJsonResponse))
                    .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value("Selected category retrieved successfully!"),
                        jsonPath("$.data.name").value(categoryResponseDto.getName()));
        }

        @Test
        @DisplayName("UTP-CC-U001: Should return response entity when update specific category")
        void shouldReturnResponseEntityWhenUpdateSpecificCategory () throws Exception {

            // Arrange
            final long testCategoryId = testCategory.getId();

            // Stub
            when(categoryService.updateCategoryData(testCategoryId, categoryUpdateDto))
                    .thenReturn(categoryResponseDto);

            String categoryJsonResponse = new ObjectMapper().writeValueAsString(categoryResponseDto);

            // Act + Stub
            mockMvc.perform(put("/api/categories/{id}", testCategoryId)
                        .with(csrf())
                        .with(httpBasic("acrexia", "dummy"))
                        .content(categoryJsonResponse)
                        .contentType("application/json"))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.message").value("Category updated successfully!"));
        }

    }

    @Nested
    class NegativeTests {

        @Test
        @DisplayName("UTN-CC-C001: Should return 409 status in create category if name already exist")
        void shouldReturn409statusInCreateCategoryIfNameAlreadyExist () throws Exception {

            // Arrange
            final String DUPLICATE_RESOURCE_FOUND = "Category name '%s' already exist!";
            CategoryRequestDto categoryRequest = CategoryTestData.sampleCategoryRequest();

            String requestJson = new ObjectMapper().writeValueAsString(categoryRequest);

            // Stub
            when(categoryService.saveCategoryData(any()))
                    .thenThrow(new DuplicateResourceFoundException
                            (String.format(DUPLICATE_RESOURCE_FOUND, categoryRequest.getName())));

            mockMvc.perform(post("/api/categories")
                            .with(csrf())
                            .with(httpBasic("acrexia", "dummy"))
                            .contentType("application/json")
                            .content(requestJson))
                    .andExpectAll(
                            status().isConflict(),
                            jsonPath("$.errorMessage")
                                    .value(String.format(DUPLICATE_RESOURCE_FOUND, categoryRequest.getName())),
                            jsonPath("$.httpStatus").value(HttpStatus.CONFLICT.value()));
        }

        @Test
        @DisplayName("UTN-CC-R001: Should return 404 status if category not found")
        void shouldReturn404StatusIfCategoryNotFound () throws Exception {

            // Arrange
            final long nonExistentId = 99L;
            final String RESOURCE_NOT_FOUND = "This ID: %d doesn't exist!";

            when(categoryService.getCategoryById(nonExistentId))
                    .thenThrow(new ResourceNotFoundException
                                    (String.format(RESOURCE_NOT_FOUND, nonExistentId)));

            // Act + Assert
            mockMvc.perform(get("/api/categories/{id}", nonExistentId)
                            .with(httpBasic("acrexia", "dummy")))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.errorMessage")
                                    .value(String.format(RESOURCE_NOT_FOUND, nonExistentId)),
                            jsonPath("$.httpStatus")
                                    .value(HttpStatus.NOT_FOUND.value()));
        }

        @Test
        @DisplayName("UTN-CC-S001: Should return 400 status if category data has missing input")
        void shouldReturn400statusIfCategoryDataHasMissingInput () throws Exception {

            // Arrange
            categoryRequestDto.setSlug(null);

            String badJsonRequest = new ObjectMapper().writeValueAsString(categoryRequestDto);

            // Act + Assert
            mockMvc.perform(post("/api/categories")
                        .with(csrf())
                        .with(httpBasic("acrexia", "dummy"))
                        .content(badJsonRequest)
                        .contentType("application/json"))
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.errorMessage[*]", everyItem(greaterThan(0))),
                            jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.value()));
        }

        @Test
        @DisplayName("UTN-CC-S002: Should return 400 status if category request has no json body")
        void shouldReturn400statusIfCategoryRequestHasNoJsonBody () throws Exception {

            // Act + Assert
            mockMvc.perform(post("/api/categories")
                            .with(httpBasic("acrexia", "dummy"))
                            .with(csrf())
                            .content("")
                            .contentType("application/json"))
                    .andExpect(status().isBadRequest());
        }
    }
}
