package com.marlonb.book_catalog_api.service;

import com.marlonb.book_catalog_api.model.CategoryEntity;
import com.marlonb.book_catalog_api.model.dto.CategoryRequestDto;
import com.marlonb.book_catalog_api.model.dto.CategoryResponseDto;
import com.marlonb.book_catalog_api.model.dto.CategoryUpdateDto;
import com.marlonb.book_catalog_api.model.mapper.CategoryMapper;
import com.marlonb.book_catalog_api.repository.CategoryRepository;

import com.marlonb.book_catalog_api.exception.custom.DataAccessException;
import com.marlonb.book_catalog_api.exception.custom.DuplicateResourceFoundException;
import com.marlonb.book_catalog_api.exception.custom.ResourceNotFoundException;
import com.marlonb.book_catalog_api.utils.test_assertions.CategoryTestAssertions;
import com.marlonb.book_catalog_api.utils.test_data.CategoryTestData;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

import static com.marlonb.book_catalog_api.utils.test_assertions.CategoryTestAssertions.constraintViolationCategoryAssertions;
import static com.marlonb.book_catalog_api.utils.test_assertions.CategoryTestAssertions.forComparingCategoryAssertions;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
@Transactional
public class CategoryServiceUnitTests {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    private CategoryRequestDto createCategoryRequest;
    private CategoryEntity testCategory;
    private CategoryResponseDto expectedResponse;
    private CategoryUpdateDto updateCategory;

    @BeforeEach
    void initGlobal() {

        // Arrange Initialization
        createCategoryRequest = CategoryTestData.sampleCategoryRequest();
        testCategory = CategoryTestData.sampleCategoryEntity();
        expectedResponse = CategoryTestData.sampleCategoryResponse();
        updateCategory = CategoryTestData.sampleCategoryUpdate();

    }

    @Nested
    @DisplayName("UTP-CS: Category Service — Unit testing: Positive tests")
    class PositiveTests {

        @Test
        @DisplayName("UTP-CSC-001: Should create new category successfully")
        void shouldCreateNewCategorySuccessfully () {

            // Stub
            Mockito.when(categoryRepository.save(Mockito.any(CategoryEntity.class)))
                    .thenReturn(testCategory);

            Mockito.when(categoryMapper.toEntity(Mockito.any(CategoryRequestDto.class)))
                    .thenReturn(testCategory);

            Mockito.when(categoryMapper.toResponse(Mockito.any(CategoryEntity.class)))
                    .thenReturn(expectedResponse);

            // Act
            CategoryResponseDto result = categoryService.saveCategoryData(createCategoryRequest);

            // Assert
            CategoryTestAssertions.forComparingCategoryAssertions(result, expectedResponse);
        }

        @Test
        @DisplayName("UTP-CSU-001: Should update category successfully")
        void shouldUpdateCategorySuccessfully () {

                // Arrange
                CategoryUpdateDto updateRequest = CategoryTestData.sampleCategoryUpdate();

                final long testCategoryId = testCategory.getId();

                // STUB
                // Find ID with repository
                Mockito.when(categoryRepository.findById(testCategoryId))
                        .thenReturn(Optional.of(testCategory));

                // Update entity
                Mockito.doNothing().when(categoryMapper).updateEntity(updateRequest, testCategory);

                // Save entity
                Mockito.when(categoryRepository.save(testCategory)).thenReturn(testCategory);

                // Map it to response
                Mockito.when(categoryMapper.toResponse(testCategory)).thenReturn(expectedResponse);

                // ACT
                CategoryResponseDto result = categoryService.updateCategoryData(testCategoryId, updateRequest);

                // ASSERT
                forComparingCategoryAssertions(result, expectedResponse);
            }

        @Test
        @DisplayName("UTP-CSR-001: Should retrieve all categories successfully")
        void shouldRetrieveAllCategoriesSuccessfully () {

            // Arrange
            CategoryEntity categoryEntity1 = CategoryTestData.sampleCategoryEntity();
            CategoryEntity categoryEntity2 = CategoryTestData.sampleNewCategoryEntity();
            CategoryResponseDto categoryResponse1 = CategoryTestData.sampleCategoryResponse();
            CategoryResponseDto categoryResponse2 = CategoryTestData.sampleNewCategoryResponse();

            List<CategoryEntity> listOfCategoryEntities = List.of(categoryEntity1, categoryEntity2);
            List<CategoryResponseDto> expectedResponses = List.of(categoryResponse1, categoryResponse2);

            // Stub
            Mockito.when(categoryRepository.findAll()).thenReturn(listOfCategoryEntities);

            Mockito.when(categoryMapper.toResponse(categoryEntity1)).thenReturn(categoryResponse1);
            Mockito.when(categoryMapper.toResponse(categoryEntity2)).thenReturn(categoryResponse2);

            // Act
            List<CategoryResponseDto> actualResponses = categoryService.getAllCategories();

            // ASSERT
            forComparingCategoryAssertions(actualResponses, expectedResponses);
        }

        @Test
        @DisplayName("UTP-CSR-002: Should retrieve category by Id successfully")
        void shouldRetrieveCategoryByIdSuccessfully () {

            // Arrange
            final long testCategoryId = testCategory.getId();

            // Stub
            Mockito.when(categoryRepository.findById(testCategoryId))
                   .thenReturn(Optional.of(testCategory));

            Mockito.when(categoryMapper.toResponse(Mockito.any(CategoryEntity.class)))
                    .thenReturn(expectedResponse);

            // Act
            CategoryResponseDto result = categoryService.getCategoryById(testCategoryId);

            // Assert
            forComparingCategoryAssertions(result, expectedResponse);
        }
    }

    @Nested
    @DisplayName("UTN-CS: Category Service — Unit testing: Negative tests")
    class NegativeTests {

        @Nested
        @DisplayName("UTN-CSE: Category Service - Entity test")
        class EntityTests {

            @Test
            @DisplayName("UTN-CSE-001: Should fail creation of new category if category name already exist")
            void shouldFailCreationOfNewCategoryIfCategoryNameAlreadyExist () {

                // Stub
                Mockito.when(categoryMapper.toEntity(Mockito.any(CategoryRequestDto.class)))
                        .thenReturn(testCategory);

                Mockito.when(categoryRepository.existsByName("Fantasy"))
                        .thenReturn(true);

                // Assert
                assertThrows(DuplicateResourceFoundException.class,
                        () -> categoryService.saveCategoryData(createCategoryRequest));

                // Verify
                Mockito.verify(categoryRepository, Mockito.never()).save(Mockito.any());
            }

            @Test
            @DisplayName("UTN-CSE-002: Should fail when category Id does not exist")
            void shouldFailWhenCategoryIdDoesNotExist () {

                // Arrange
                final long nonExistentId = 999L;

                // Stub
                Mockito.when(categoryRepository.findById(nonExistentId))
                       .thenReturn(Optional.empty());

                // Assert
                assertThrows(ResourceNotFoundException.class,
                        () -> categoryService.findCategoryId(nonExistentId));

                // Verify
                Mockito.verify(categoryRepository).findById(nonExistentId);
            }

            @Test
            @DisplayName("UTN-CSE-003: Should fail when database fails")
            void shouldFailWhenDatabaseFails () {

                // Arrange
                final String DATABASE_ACCESS_FAILED = "Database Connection failed";

                // Stub
                Mockito.when(categoryMapper.toEntity(Mockito.any(CategoryRequestDto.class)))
                        .thenThrow(new DataAccessException(DATABASE_ACCESS_FAILED));

                // Act + Assert
                assertThrows(DataAccessException.class,
                        () -> categoryService.saveCategoryData(createCategoryRequest));
            }

            @Test
            @DisplayName("UTN-CSE-001: Should fail update of category if category name already exist")
            void shouldFailUpdateOfCategoryIfCategoryNameAlreadyExist () {

                // Arrange
                final long testCategoryId = testCategory.getId();
                CategoryUpdateDto updateRequest = new CategoryUpdateDto();
                updateRequest.setName("Fantasy");

                // Simulate existing category being updated
                testCategory.setName("Adventure");

                // STUB
                Mockito.when(categoryRepository.findById(testCategoryId))
                        .thenReturn(Optional.of(testCategory));

                Mockito.when(categoryRepository.existsByName("Fantasy"))
                        .thenReturn(true);

                // Act + Assert
                Assertions.assertThrows(DuplicateResourceFoundException.class,
                        () -> categoryService.updateCategoryData(testCategoryId, updateRequest));

                // Verify
                Mockito.verify(categoryRepository, Mockito.never()).save(Mockito.any());
            }
        }

        @Nested
        @DisplayName("UTN-CSV: Category Service - Validation test")
        class ValidationTests {

            @Nested
            class StringAttributeTests {

                @ParameterizedTest
                @NullAndEmptySource
                @ValueSource(strings = {"", "Lorem ipsum dolor sit amet, " +
                                        "consectetur adipiscing elit sed do."})
                @DisplayName("UTN-CSV-S001: Should fail when category name is not valid")
                void shouldFailWhenCategoryNameIsNotValid(String invalidNames) {

                    // Arrange
                    createCategoryRequest.setName(invalidNames);
                    updateCategory.setName(invalidNames);

                    // Act + Assert
                    constraintViolationCategoryAssertions(createCategoryRequest, updateCategory);
                }

                @ParameterizedTest
                @NullAndEmptySource
                @ValueSource(strings = {"-slug", "--slug", "slug-", "slug--",
                                        "slug--name", "-slug-name-","-slug-","Slug"})
                @DisplayName("UTN-CSV-S002: Should fail when category slug is not valid")
                void shouldFailWhenCategorySlugIsNotValid (String invalidSlugs) {

                    // Arrange
                    createCategoryRequest.setSlug(invalidSlugs);
                    updateCategory.setSlug(invalidSlugs);

                    // Act + Assert
                    constraintViolationCategoryAssertions(createCategoryRequest, updateCategory);
                }

                @ParameterizedTest
                @ValueSource(strings = {"Lorem ipsum dolor sit amet, consectetur adipiscing " +
                            "elit. Sed do eiusmod tempor incididunt ut labore et dolore " +
                            "magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation " +
                            "ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute " +
                            "irure dolor in reprehenderit in voluptate."})
                @DisplayName("UTN-CSV-S003: Should fail when category description exceeds limit")
                void shouldFailWhenCategoryDescriptionExceedsLimit (String invalidDescription){

                    // Arrange
                    createCategoryRequest.setDescription(invalidDescription);
                    updateCategory.setDescription(invalidDescription);

                    // Act + Assert
                    constraintViolationCategoryAssertions(createCategoryRequest, updateCategory);
                }
            }

            @Nested
            class BooleanAttributeTests{

                @Test
                @DisplayName("UTN-CSV-B001: Should fail when Active Status is null")
                void shouldFailWhenActiveStatusIsNull(){

                    // Arrange
                    createCategoryRequest.setIsActive(null);
                    updateCategory.setIsActive(null);

                    // Act + Assert
                    constraintViolationCategoryAssertions(createCategoryRequest, updateCategory);
                }
            }
        }
    }
}
