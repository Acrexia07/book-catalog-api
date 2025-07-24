package com.marlonb.book_catalog_api.utils.test_data;

import com.marlonb.book_catalog_api.model.BookEntity;
import com.marlonb.book_catalog_api.model.dto.BookRequestDto;
import com.marlonb.book_catalog_api.model.dto.BookResponseDto;
import com.marlonb.book_catalog_api.model.dto.BookUpdateDto;


public class BookTestData {

    // ENTITY: BOOK
    public static BookEntity sampleBookEntity () {

        var sampleBook = new BookEntity ();
        sampleBook.setId(1L);
        sampleBook.setTitle("Harry Potter");
        sampleBook.setAuthor("J.K.Rowling");
        sampleBook.setPages(400);
        sampleBook.setCategory(CategoryTestData.sampleCategoryEntity());
        sampleBook.setIsInStock(true);

        return sampleBook;
    }

    public static BookEntity sampleBookEntity2 () {

        var sampleBook = new BookEntity ();
        sampleBook.setId(3L);
        sampleBook.setTitle("Harry Potter and the Philosopher's Stone");
        sampleBook.setAuthor("J.K.Rowling");
        sampleBook.setPages(550);
        sampleBook.setCategory(CategoryTestData.sampleCategoryEntity());
        sampleBook.setIsInStock(true);

        return sampleBook;
    }

    public static BookEntity newSampleBookEntity () {

        var newSampleBook = new BookEntity();
        newSampleBook.setId(2L);
        newSampleBook.setTitle("The Whispering Shadows");
        newSampleBook.setAuthor("Sarah H. Blackwood");
        newSampleBook.setPages(342);
        newSampleBook.setCategory(CategoryTestData.sampleNewCategoryEntity());
        newSampleBook.setIsInStock(true);

        return newSampleBook;
    }

    // DTO: RESPONSE
    public static BookResponseDto sampleBookResponse () {

        return new BookResponseDto(
                sampleBookEntity().getId(),
                sampleBookEntity().getTitle(),
                sampleBookEntity().getAuthor(),
                CategoryTestData.sampleCategoryResponse(),
                sampleBookEntity().getPages(),
                sampleBookEntity().getIsInStock()
        );
    }

    public static BookResponseDto sampleBookResponse2 () {

        return new BookResponseDto(
                sampleBookEntity2().getId(),
                sampleBookEntity2().getTitle(),
                sampleBookEntity2().getAuthor(),
                CategoryTestData.sampleCategoryResponse(),
                sampleBookEntity2().getPages(),
                sampleBookEntity2().getIsInStock()
        );
    }

    public static BookResponseDto sampleNewBookResponse () {

        return new BookResponseDto (
                newSampleBookEntity().getId(),
                newSampleBookEntity().getTitle(),
                newSampleBookEntity().getAuthor(),
                CategoryTestData.sampleCategoryResponse(),
                newSampleBookEntity().getPages(),
                newSampleBookEntity().getIsInStock()
        );
    }


    //DTO: REQUEST
    public static BookRequestDto sampleBookRequest () {

        return new BookRequestDto(
                sampleBookEntity().getTitle(),
                sampleBookEntity().getAuthor(),
                sampleBookEntity().getCategory().getId(),
                sampleBookEntity().getPages(),
                sampleBookEntity().getIsInStock()
        );
    }

    //DTO: UPDATE
    public static BookUpdateDto sampleBookUpdate () {

        return new BookUpdateDto(
                sampleBookEntity().getTitle(),
                sampleBookEntity().getAuthor(),
                sampleBookEntity().getCategory().getId(),
                500,
                sampleBookEntity().getIsInStock()
        );
    }
}
