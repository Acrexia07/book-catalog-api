package com.marlonb.book_catalog_api.repository;

import com.marlonb.book_catalog_api.model.BookEntity;
import com.marlonb.book_catalog_api.model.CategoryEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class BookRepositoryIntegrationTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("ITP-BR-S001: Should return true when Book exists by title")
    void shouldReturnTrueWhenBookExistsByTitle () {

        // Given
        CategoryEntity category = new CategoryEntity();
        category.setName("Fiction");
        category.setSlug("fiction");
        category.setDescription("Nice genre");
        category.setIsActive(true);
        entityManager.persistAndFlush(category);

        BookEntity book = new BookEntity();
        book.setTitle("Harry Potter");
        book.setCategory(category);
        entityManager.persistAndFlush(book);

        // When
        boolean exists = bookRepository.existsByTitle("Harry Potter");

        // Then
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should retrieve all books by category Id ")
    void shouldRetrieveAllBooksByCategoryId (){

        // Given
        CategoryEntity category = new CategoryEntity();
        category.setName("Fiction");
        category.setSlug("fiction");
        category.setDescription("Nice genre");
        category.setIsActive(true);
        entityManager.persistAndFlush(category);

        BookEntity book1 = new BookEntity();
        book1.setTitle("Harry Potter");
        book1.setCategory(category);
        entityManager.persistAndFlush(book1);

        BookEntity book2 = new BookEntity();
        book2.setTitle("Harry Potter and the Philosopher's Stone");
        book2.setCategory(category);
        entityManager.persistAndFlush(book2);

        // When
        List<BookEntity> listOfBooks = bookRepository.findAllBooksByCategoryId(category.getId());

        // Then
        Assertions.assertThat(listOfBooks).hasSize(2);
    }

    @Test
    @DisplayName("ITN-BR-S001: Should return false when Book does not exists by title")
    void shouldReturnFalseWhenBookExistsByTitle () {

        // Given
        boolean exists = bookRepository.existsByTitle("Non-existent title");

        // Then
        Assertions.assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should return empty list when no books exists for category id ")
    void shouldReturnEmptyListWhenNoBooksExistsForCategoryId () {

        // Given
        CategoryEntity category = new CategoryEntity();
        category.setName("Non-fiction");
        category.setSlug("non-fiction");
        category.setDescription("No books yet");
        category.setIsActive(true);
        entityManager.persistAndFlush(category);

        // When
        List<BookEntity> listOfBooks = bookRepository.findAllBooksByCategoryId(category.getId());

        // Then
        Assertions.assertThat(listOfBooks).isEmpty();
    }
}
