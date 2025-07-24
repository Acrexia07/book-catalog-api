package com.marlonb.book_catalog_api.service;

import com.marlonb.book_catalog_api.model.BookEntity;
import com.marlonb.book_catalog_api.model.CategoryEntity;
import com.marlonb.book_catalog_api.model.dto.*;
import com.marlonb.book_catalog_api.model.mapper.BookMapper;
import com.marlonb.book_catalog_api.repository.BookRepository;
import com.marlonb.book_catalog_api.exception.custom.ResourceNotFoundException;
import com.marlonb.book_catalog_api.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CategoryRepository categoryRepository;

    private static final String ID_NOT_FOUND = "This %s ID: %d doesn't exist!";

    public BookService (BookRepository bookRepository,
                        BookMapper bookMapper,
                        CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.categoryRepository = categoryRepository;
    }

    // CREATE: Add new book
    @Transactional
    public BookResponseDto saveBookData (BookRequestDto bookRequestDto){

        BookEntity createBookRequest = bookMapper.toEntity(bookRequestDto);
        final long categoryId = createBookRequest.getCategory().getId();

        /*
            Declare a category entity object that throws ResourceNotFoundException
            if there's no book id found in the category
        */
        CategoryEntity targetCategory =
                categoryRepository.findById(bookRequestDto.getCategoryId()).orElseThrow(
                        () -> new ResourceNotFoundException(
                                    (String.format(ID_NOT_FOUND, "Category", categoryId)
                                )));

        createBookRequest.setCategory(targetCategory);
        BookEntity savedBook = bookRepository.save(createBookRequest);
        return bookMapper.toResponse(savedBook);
    }

    // READ (All): Retrieve all books
    @Transactional(readOnly = true)
    public List<BookResponseDto> getAllBooks () {

        List<BookEntity> listOfBooks = bookRepository.findAll();

        // Map all book entities to response DTO using bookMapper
        return listOfBooks.stream()
                          .map(bookMapper::toResponse)
                          .toList();
    }

    // READ (Specific): Retrieve book by id
    @Transactional(readOnly = true)
    public BookResponseDto getBookById (long id) {

        BookEntity foundBook = findBookId(id);
        return bookMapper.toResponse(foundBook);
    }

    // READ (ALL): Retrieve all books by category ID
    @Transactional(readOnly = true)
    public List<BookResponseDto> getAllBooksByCategoryId (long id) {

        // Throws ResourceNotFoundException if category id doesn't exist
        if(!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException(String.format(ID_NOT_FOUND, "Category", id));
        }

        List<BookEntity> foundListOfBooks = bookRepository.findAllBooksByCategoryId(id);
        return bookMapper.toResponse(foundListOfBooks);
    }

    // UPDATE: Modify existing book by id
    @Transactional
    public BookResponseDto updateBook (long id,
                                       BookUpdateDto updateBook) {

        BookEntity foundBook = findBookId(id);
        bookMapper.updateEntity(updateBook, foundBook);

        /*
            Declare a category entity object that throws ResourceNotFoundException
            if there's no book id found in the category
        */
        CategoryEntity targetCategory =
                categoryRepository.findById(foundBook.getId()).orElseThrow(
                        () -> new ResourceNotFoundException(
                                (String.format(ID_NOT_FOUND, "Category", id)
                                )));

        foundBook.setCategory(targetCategory);
        BookEntity updateFoundBook = bookRepository.save(foundBook);
        return bookMapper.toResponse(updateFoundBook);
    }

    // DELETE: Remove selected book by ID
    @Transactional
    public void deleteBookById (long id) {

        findBookId(id);
        bookRepository.deleteById(id);
    }

    // REUSABLE METHOD: For find id
    public BookEntity findBookId (long id) {

        return bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException
                        (String.format(ID_NOT_FOUND, "Book", id)));
    }
}
