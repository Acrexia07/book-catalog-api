package com.marlonb.book_catalog_api.service;

import com.marlonb.book_catalog_api.model.BookEntity;
import com.marlonb.book_catalog_api.model.CategoryEntity;
import com.marlonb.book_catalog_api.model.dto.BookRequestDto;
import com.marlonb.book_catalog_api.model.dto.BookResponseDto;
import com.marlonb.book_catalog_api.model.dto.BookUpdateDto;
import com.marlonb.book_catalog_api.model.mapper.BookMapper;
import com.marlonb.book_catalog_api.repository.BookRepository;
import com.marlonb.book_catalog_api.exception.custom.DataAccessException;
import com.marlonb.book_catalog_api.exception.custom.DuplicateResourceFoundException;
import com.marlonb.book_catalog_api.exception.custom.ResourceNotFoundException;
import com.marlonb.book_catalog_api.repository.CategoryRepository;
import com.marlonb.book_catalog_api.utils.test_data.BookTestData;
import com.marlonb.book_catalog_api.utils.test_data.CategoryTestData;
import org.assertj.core.api.Assertions;
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

import static com.marlonb.book_catalog_api.utils.test_assertions.BookTestAssertions.constraintViolationBookAssertions;
import static com.marlonb.book_catalog_api.utils.test_assertions.BookTestAssertions.forComparingBookAssertions;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
@Transactional
public class BookServiceUnitTests {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    private BookEntity testBook;
    private BookRequestDto createBookRequest;
    private BookResponseDto expectedResponse;
    private BookUpdateDto bookUpdate;

    @BeforeEach
    void initGlobal () {

        // Arrange initialization
        testBook = BookTestData.sampleBookEntity();
        createBookRequest = BookTestData.sampleBookRequest();
        expectedResponse = BookTestData.sampleBookResponse();
        bookUpdate = BookTestData.sampleBookUpdate();
    }

    @Nested
    @DisplayName("UTP-BS: Book Service — Unit testing: Positive tests")
    class PositiveTests {

        @Test
        @DisplayName("UTP-BSC-001: Should create new book successfully")
        void shouldCreateNewBookSuccessfully() {

            // Arrange
            CategoryEntity targetCategory = CategoryTestData.sampleCategoryEntity();

            // Stub
            Mockito.when(categoryRepository.findById(testBook.getCategory().getId()))
                    .thenReturn(Optional.of(targetCategory));

            Mockito.when(bookRepository.save(Mockito.any(BookEntity.class)))
                    .thenReturn(testBook);

            Mockito.when(bookMapper.toEntity(Mockito.any(BookRequestDto.class)))
                    .thenReturn(testBook);

            Mockito.when(bookMapper.toResponse(Mockito.any(BookEntity.class)))
                    .thenReturn(expectedResponse);

            // Act
            BookResponseDto actualResponse = bookService.saveBookData(createBookRequest);

            // Assert
            forComparingBookAssertions(expectedResponse, actualResponse);

            // Verify
            Mockito.verify(categoryRepository).findById(targetCategory.getId());
            Mockito.verify(bookRepository).save(Mockito.any(BookEntity.class));
            Mockito.verify(bookMapper).toEntity(createBookRequest);
            Mockito.verify(bookMapper).toResponse(testBook);
        }

        @Test
        @DisplayName("UTP-BSU-001: Should update book successfully")
        void shouldUpdateBookSuccessfully () {

            //Arrange
            CategoryEntity targetCategory = CategoryTestData.sampleCategoryEntity();
            final long testBookId = testBook.getId();

            // Stub
            // Find specific book
            Mockito.when(bookRepository.findById(testBookId))
                    .thenReturn(Optional.of(testBook));

            Mockito.when(categoryRepository.findById(testBookId))
                    .thenReturn(Optional.of(targetCategory));

            // map → update dto
            Mockito.doNothing()
                    .when(bookMapper)
                    .updateEntity(bookUpdate, testBook);

            // save mapped update DTO
            Mockito.when(bookRepository.save(testBook))
                    .thenReturn(testBook);

            // pass → response DTO
            Mockito.when(bookMapper.toResponse(testBook))
                    .thenReturn(expectedResponse);

            // Act
            BookResponseDto actualResponse = bookService.updateBook(testBookId, bookUpdate);

            // Assert
            forComparingBookAssertions(actualResponse, expectedResponse);
        }

        @Test
        @DisplayName("UTP-BSR-001: Should retrieve all books successfully")
        void shouldRetrieveAllBooksSuccessfully () {

            // Arrange
            BookEntity testBook1 = BookTestData.sampleBookEntity();
            BookEntity testBook2 = BookTestData.newSampleBookEntity();

            BookResponseDto bookResponse1 = BookTestData.sampleBookResponse();
            BookResponseDto bookResponse2 = BookTestData.sampleNewBookResponse();

            List<BookEntity> listOfBooks = List.of(testBook1, testBook2);
            List<BookResponseDto> expectedResponses = List.of(bookResponse1, bookResponse2);

            // Stub
            Mockito.when(bookRepository.findAll()).thenReturn(listOfBooks);

            Mockito.when(bookMapper.toResponse(testBook1)).thenReturn(bookResponse1);
            Mockito.when(bookMapper.toResponse(testBook2)).thenReturn(bookResponse2);

            // Act
            List<BookResponseDto> actualResponses = bookService.getAllBooks();

            // Assert
            forComparingBookAssertions(expectedResponses, actualResponses);
        }

        @Test
        @DisplayName("UTP-BSR-001: Should retrieve all books by category id successfully")
        void shouldRetrieveAllBooksByCategoryIdSuccessfully () {

            //Arrange
            BookEntity testBook1 = BookTestData.sampleBookEntity();
            BookEntity testBook2 = BookTestData.sampleBookEntity2();

            final long testCategoryId = BookTestData.sampleBookEntity().getCategory().getId();

            BookResponseDto bookResponse1 = BookTestData.sampleBookResponse();
            BookResponseDto bookResponse2 = BookTestData.sampleBookResponse2();

            List<BookEntity> listOfBooks = List.of(testBook1, testBook2);
            List<BookResponseDto> expectedResponses = List.of(bookResponse1, bookResponse2);

            // Stub
            Mockito.when(categoryRepository.existsById(testCategoryId)).thenReturn(true);

            Mockito.when(bookRepository.findAllBooksByCategoryId(testCategoryId))
                    .thenReturn(listOfBooks);

            Mockito.when(bookMapper.toResponse(listOfBooks))
                    .thenReturn(expectedResponses);

            List<BookResponseDto> actualResponses =
                    bookService.getAllBooksByCategoryId(testCategoryId);

            // Assert
            forComparingBookAssertions(actualResponses, expectedResponses);
        }

        @Test
        @DisplayName("UTP-BSR-002: Should retrieve book by Id successfully")
        void shouldRetrieveBookByIdSuccessfully () {

            // Arrange
            final long testBookId = testBook.getId();

            // Stub
            Mockito.when(bookRepository.findById(testBookId))
                    .thenReturn(Optional.of(testBook));

            Mockito.when(bookMapper.toResponse(Mockito.any(BookEntity.class)))
                    .thenReturn(expectedResponse);

            // Act
            BookResponseDto actualResponse = bookService.getBookById(testBookId);

            // Assert
            forComparingBookAssertions(actualResponse, expectedResponse);
        }

        @Test
        @DisplayName("UTP-BSD-001: Should delete selected book successfully!")
        void shouldDeleteSelectedBookSuccessfully () {

            // Arrange
            final long testBookId = testBook.getId();

            // Stub
            Mockito.when(bookRepository.findById(testBookId))
                    .thenReturn(Optional.of(testBook));

            // Act
            bookService.deleteBookById(testBookId);

            // Verify
            Mockito.verify(bookRepository).deleteById(testBookId);
        }

    }

    @Nested
    @DisplayName("UTN-BS: Book Service — Unit testing: Negative tests")
    class NegativeTests {

        @Nested
        class EntityTests {
            @Test
            @DisplayName("UTN-BSE-001: Should fail when book not found")
            void shouldFailWhenBookNotFound () {

                // Arrange
                final long nonExistentID = 999L;

                // Stub
                Mockito.when(bookRepository.findById(nonExistentID))
                        .thenReturn(Optional.empty());

                // Act + Assert
                assertThrows(ResourceNotFoundException.class,
                        () -> bookService.findBookId(nonExistentID));

                // Verify
                Mockito.verify(bookRepository).findById(nonExistentID);
            }

            @Test
            @DisplayName("UTN-BSE-003: Should fail when database fails")
            void shouldFailWhenDatabaseFails () {

                // Arrange
                final String DATABASE_ACCESS_FAILED = "Database Connection failed";

                // Stub
                Mockito.when(bookMapper.toEntity(Mockito.any(BookRequestDto.class)))
                        .thenThrow(new DataAccessException(DATABASE_ACCESS_FAILED));

                // Act + Assert
                assertThrows(DataAccessException.class,
                        () -> bookService.saveBookData(createBookRequest));
            }
        }

        @Nested
        class BookAttributeValidationTests {

            @ParameterizedTest
            @NullAndEmptySource
            @ValueSource(strings = {"","Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                                    "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                                    "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris " +
                                    "nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in " +
                                    "reprehenderit in voluptate."})
            @DisplayName("UTN-BSV-S001: Should fail when book title is not valid")
            void shouldFailWhenBookTitleIsNotValid (String invalidTitles) {

                // Arrange
                createBookRequest.setTitle(invalidTitles);
                bookUpdate.setTitle(invalidTitles);

                // Act + Assert
                constraintViolationBookAssertions(createBookRequest, bookUpdate);
            }

            @ParameterizedTest
            @NullAndEmptySource
            @ValueSource(strings = {"","Lorem ipsum dolor sit amet, consectetur " +
                                    "adipiscing elit sed do."})
            @DisplayName("UTN-BSV-S002: Should fail when book author is not valid")
            void shouldFailWhenBookAuthorIsNotValid (String invalidAuthors) {

                // Arrange
                createBookRequest.setAuthor(invalidAuthors);
                bookUpdate.setAuthor(invalidAuthors);

                // Act + Assert
                constraintViolationBookAssertions(createBookRequest, bookUpdate);
            }

            @Test
            @DisplayName("UTN-BSV-S003: Should fail when book category ID is not valid")
            void shouldFailWhenBookCategoryIdIsNotValid () {

                // Arrange
                createBookRequest.setCategoryId(null);
                bookUpdate.setCategoryId(null);

                // Act + Assert
                constraintViolationBookAssertions(createBookRequest, bookUpdate);
            }

            @ParameterizedTest
            @ValueSource(ints = {0, -1, -999})
            @DisplayName("UTN-BSV-S003: Should fail when book page is not valid")
            void shouldFailWhenBookPageIsNotValid (Integer invalidPages) {

                // Arrange
                createBookRequest.setPages(invalidPages);
                bookUpdate.setPages(invalidPages);

                // Act + Assert
                constraintViolationBookAssertions(createBookRequest, bookUpdate);
            }

            @Test
            @DisplayName("UTN-BSV-S003: Should fail when Book Stock status is not valid")
            void shouldFailWhenBookStockStatusIsNotValid () {

                // Arrange
                createBookRequest.setIsInStock(null);
                bookUpdate.setIsInStock(null);

                // Act + Assert
                constraintViolationBookAssertions(createBookRequest, bookUpdate);

            }
        }
    }
}
