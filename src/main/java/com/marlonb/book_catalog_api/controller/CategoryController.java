package com.marlonb.book_catalog_api.controller;

import com.marlonb.book_catalog_api.model.dto.CategoryRequestDto;
import com.marlonb.book_catalog_api.model.dto.CategoryResponseDto;
import com.marlonb.book_catalog_api.model.dto.CategoryUpdateDto;
import com.marlonb.book_catalog_api.service.CategoryService;
import com.marlonb.book_catalog_api.utils.ApiResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.marlonb.book_catalog_api.utils.ApiMessageHandler.getApiMessage;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // POST: CREATE NEW CATEGORY
    @PostMapping(path = "/categories")
    public ResponseEntity<ApiResponseDto<CategoryResponseDto>> createNewCategory (@Valid @RequestBody
                                                                  CategoryRequestDto categoryRequest) {

        CategoryResponseDto createCategory = categoryService.saveCategoryData(categoryRequest);
        URI location = URI.create("/api/categories/" + createCategory.getId());
        return ResponseEntity.created(location).body(getApiMessage("Category created successfully!",
                                                                   createCategory));
    }

    // GET: RETRIEVE ALL CATEGORIES
    @GetMapping(path = "/categories")
    public ResponseEntity<ApiResponseDto<List<CategoryResponseDto>>> retrieveAllCategories () {

        List<CategoryResponseDto> listOfCategories = categoryService.getAllCategories();
        // Return a 200 OK response with the API message containing the list of categories
        return ResponseEntity.ok(getApiMessage("Categories retrieved successfully!",
                                               listOfCategories));
    }

    // GET: RETRIEVE SPECIFIC CATEGORY BY ID
    @GetMapping(path = "/categories/{id}")
    public ResponseEntity<ApiResponseDto<CategoryResponseDto>> retrieveSelectedCategory (@PathVariable long id) {

        CategoryResponseDto selectedCategory = categoryService.getCategoryById(id);
        // Return a 200 OK response with the API message containing the selected category
        return ResponseEntity.ok(getApiMessage("Selected category retrieved successfully!",
                                               selectedCategory));
    }

    // PUT: UPDATE SELECTED CATEGORY BY ID
    @PutMapping(path = "/categories/{id}")
    public ResponseEntity<ApiResponseDto<CategoryResponseDto>> updateSelectedCategory (@PathVariable long id,
                                                                                       @Valid @RequestBody
                                                                       CategoryUpdateDto updateDto) {

        CategoryResponseDto updatedCategory = categoryService.updateCategoryData(id, updateDto);
        // Return a 200 OK response with the API message containing the updated category
        return ResponseEntity.ok(getApiMessage("Category updated successfully!",
                                               updatedCategory));
    }
}
