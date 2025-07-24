package com.marlonb.book_catalog_api.repository;

import com.marlonb.book_catalog_api.model.CategoryEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class CategoryRepositoryIntegrationTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("ITP-CR-S001: Should return true when category exists by name")
    void shouldReturnTrueWhenCategoryExistsByName () {

        // Given
        CategoryEntity category = new CategoryEntity();
        category.setName("Fiction");
        category.setSlug("fiction");
        category.setIsActive(true);
        entityManager.persistAndFlush(category);

        // When
        boolean exists = categoryRepository.existsByName("Fiction");

        // Then
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("ITN-CR-S001: Should return false when category does not exists by name")
    void shouldReturnTrueWhenCategoryDoesNotExistsByName () {

        // When
        boolean exists = categoryRepository.existsByName("Non-existent Name");

        // Then
        Assertions.assertThat(exists).isFalse();
    }
}
