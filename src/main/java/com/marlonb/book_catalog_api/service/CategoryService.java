package com.marlonb.book_catalog_api.service;

import com.marlonb.book_catalog_api.model.CategoryEntity;
import com.marlonb.book_catalog_api.model.dto.CategoryRequestDto;
import com.marlonb.book_catalog_api.model.dto.CategoryResponseDto;
import com.marlonb.book_catalog_api.model.dto.CategoryUpdateDto;
import com.marlonb.book_catalog_api.model.mapper.CategoryMapper;
import com.marlonb.book_catalog_api.repository.CategoryRepository;
import com.marlonb.book_catalog_api.exception.custom.DuplicateResourceFoundException;
import com.marlonb.book_catalog_api.exception.custom.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private static final String CATEGORY_NOT_FOUND = "This ID: %d doesn't exist!";
    private static final String DUPLICATE_RESOURCE_FOUND = "Category name '%s' already exist!";

    public CategoryService(CategoryRepository categoryRepository,
                           CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    // CREATE: Add new category
    @Transactional
    public CategoryResponseDto saveCategoryData (CategoryRequestDto createRequest) {

        CategoryEntity categoryFromRequest = categoryMapper.toEntity(createRequest);

        // Throw DuplicateResourceFoundException if categoryFromRequest's name already exists
        if(categoryRepository.existsByName(categoryFromRequest.getName())){
            throw new DuplicateResourceFoundException
                            (String.format(DUPLICATE_RESOURCE_FOUND, categoryFromRequest.getName()));
        }

        CategoryEntity saveCategory = categoryRepository.save(categoryFromRequest);
        return categoryMapper.toResponse(saveCategory);
    }

    // READ: View all category
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories(){

        List<CategoryEntity> listOfCategories = categoryRepository.findAll();

        // Map all category entities to response DTOs using categoryMapper
        return listOfCategories.stream()
                               .map(categoryMapper::toResponse)
                               .toList();
    }

    // READ: View selected category by ID
    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryById (long id) {

        CategoryEntity foundCategoryId = findCategoryId(id);
        return categoryMapper.toResponse(foundCategoryId);
    }

    // UPDATE:
    @Transactional
    public CategoryResponseDto updateCategoryData (long id, CategoryUpdateDto updateRequest){

        CategoryEntity foundCategoryForUpdate = findCategoryId(id);

        /*
            Throw DuplicateResourceFoundException if updateRequest's name already exist and
            foundCategoryForUpdate is not equal to the updateRequest's name (different from
            the current category name)
        */
        if(categoryRepository.existsByName(updateRequest.getName()) &&
                !foundCategoryForUpdate.getName().equalsIgnoreCase(updateRequest.getName())){
            throw new DuplicateResourceFoundException
                    (String.format(DUPLICATE_RESOURCE_FOUND, updateRequest.getName()));
        }

        categoryMapper.updateEntity(updateRequest, foundCategoryForUpdate);
        categoryRepository.save(foundCategoryForUpdate);
        return categoryMapper.toResponse(foundCategoryForUpdate);
    }

    // For find id
    public CategoryEntity findCategoryId (long id) {

        // Retrieve category by ID, or throw an exception if not found
        return categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException
                        (String.format(CATEGORY_NOT_FOUND, id)));
    }
}
