package com.marlonb.book_catalog_api.repository;

import com.marlonb.book_catalog_api.model.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    boolean existsByName (String name);
}
