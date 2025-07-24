package com.marlonb.book_catalog_api.repository;

import com.marlonb.book_catalog_api.model.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    List<BookEntity> findAllBooksByCategoryId (long categoryId);
    boolean existsByTitle (String title);
}
