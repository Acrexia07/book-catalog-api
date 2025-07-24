package com.marlonb.book_catalog_api.controller;

import com.marlonb.book_catalog_api.model.dto.BookRequestDto;
import com.marlonb.book_catalog_api.model.dto.BookResponseDto;
import com.marlonb.book_catalog_api.model.dto.BookUpdateDto;
import com.marlonb.book_catalog_api.service.BookService;
import com.marlonb.book_catalog_api.utils.ApiResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // POST: Create new resource (book)
    @PostMapping("/books")
    public ResponseEntity<ApiResponseDto<BookResponseDto>> createNewBook (@Valid @RequestBody
                                                                       BookRequestDto bookRequest) {

        BookResponseDto createBook = bookService.saveBookData(bookRequest);
        URI location = URI.create("/api/books/" + createBook.getId());
        // Return a ResponseEntity with the location header and API response body
        return ResponseEntity.created(location)
                             .body(new ApiResponseDto<>("Book created Successfully!",
                                                       createBook));
    }

    // READ: Retrieve all resource (books)
    @GetMapping("/books")
    public ResponseEntity<ApiResponseDto<List<BookResponseDto>>> retrieveAllBooks () {

        List<BookResponseDto> lisOfBookResponses = bookService.getAllBooks();
        ApiResponseDto<List<BookResponseDto>> response = new ApiResponseDto<>(
                "Books retrieved successfully!",
                lisOfBookResponses
        );
        return ResponseEntity.ok(response);
    }

    // READ: Retrieve all resources (books by category id)
    @GetMapping("/categories/{id}/books")
    public ResponseEntity<ApiResponseDto<List<BookResponseDto>>> retrieveAllBooksByCategoryId
                                                                (@PathVariable long id) {

        List<BookResponseDto> listOfBookResponsesByCategoryId =
                                bookService.getAllBooksByCategoryId(id);
        ApiResponseDto<List<BookResponseDto>> response = new ApiResponseDto<>(
                "Books by category id retrieved successfully!",
                listOfBookResponsesByCategoryId
        );
        return ResponseEntity.ok(response);
    }

    // READ: Retrieve specific resource (book)
    @GetMapping("/books/{id}")
    public ResponseEntity<ApiResponseDto<BookResponseDto>> retrieveBookById (@PathVariable long id) {

        BookResponseDto selectedBook = bookService.getBookById(id);
        ApiResponseDto<BookResponseDto> response = new ApiResponseDto<>(
                "Specific book retrieved successfully!",
                selectedBook
        );
        return ResponseEntity.ok(response);
    }

    // UPDATE: update specific resource (book)
    @PutMapping("/books/{id}")
    public ResponseEntity <ApiResponseDto<BookResponseDto>> updateBookById (@PathVariable long id,
                                                                            @Valid @RequestBody
                                                                            BookUpdateDto bookUpdate) {

        BookResponseDto updateBook = bookService.updateBook(id, bookUpdate);
        ApiResponseDto<BookResponseDto> response = new ApiResponseDto<>(
                "Specific book updated successfully!",
                updateBook
        );
        return ResponseEntity.ok(response);
    }

    // DELETE: delete specific resource (booK)
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteSelectedBook (@PathVariable long id) {

        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }
}
