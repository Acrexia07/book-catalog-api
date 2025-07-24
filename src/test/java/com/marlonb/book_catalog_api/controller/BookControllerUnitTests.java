package com.marlonb.book_catalog_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marlonb.book_catalog_api.exception.custom.DuplicateResourceFoundException;
import com.marlonb.book_catalog_api.exception.custom.ResourceNotFoundException;
import com.marlonb.book_catalog_api.model.BookEntity;
import com.marlonb.book_catalog_api.model.dto.BookRequestDto;
import com.marlonb.book_catalog_api.model.dto.BookResponseDto;
import com.marlonb.book_catalog_api.model.dto.BookUpdateDto;
import com.marlonb.book_catalog_api.service.BookService;
import com.marlonb.book_catalog_api.utils.test_data.BookTestData;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(BookController.class)
public class BookControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    private BookRequestDto bookRequestDto;
    private BookResponseDto bookResponseDto;

    @TestConfiguration
    static class TestConfig {

        @Bean
        public BookService bookService () {
            return mock(BookService.class);
        }
    }

    @BeforeEach
    void initSetup () {

        // Arrange Initialization
        bookRequestDto = BookTestData.sampleBookRequest();
        bookResponseDto = BookTestData.sampleBookResponse();

        // Reset specific mock
        reset(bookService);
    }

    @Nested
    class PositiveTests {

        @Test
        @DisplayName("UTP-BC-C001: Should return response entity when create new book")
        void shouldReturnResponseEntityWhenCreateNewBook () throws Exception {

            // Stub
            when(bookService.saveBookData(any())).thenReturn(bookResponseDto);

            String bookJsonRequest = new ObjectMapper().writeValueAsString(bookRequestDto);

            mockMvc.perform(post("/api/books")
                        .with(csrf())
                        .with(httpBasic("acrexia", "dummy"))
                        .contentType("application/json")
                        .content(bookJsonRequest))
                    .andExpectAll(
                            status().isCreated(),
                            jsonPath("$.message").value("Book created Successfully!"),
                            jsonPath("$.data.title").value(bookRequestDto.getTitle()));
        }

        @Test
        @DisplayName("UTP-BC-R001: Should return response entity when read all books")
        void shouldReturnResponseEntityWhenReadNewBook () throws Exception {

            // Arrange
            BookResponseDto bookResponse1 = BookTestData.sampleBookResponse();
            BookResponseDto bookResponse2 = BookTestData.sampleBookResponse2();

            List<BookResponseDto> expectedResponses = List.of(bookResponse1, bookResponse2);

            // Stub
            when(bookService.getAllBooks()).thenReturn(expectedResponses);

            String listOfJsonResponses = new ObjectMapper().writeValueAsString(expectedResponses);

            // Act + Assert
            mockMvc.perform(get("/api/books")
                            .with(httpBasic("acrexia", "dummy"))
                            .contentType("application/json")
                            .content(listOfJsonResponses))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath(".message").value("Books retrieved successfully!"),
                            jsonPath("$.data[*]").isArray(),
                            jsonPath(".data[*].title", hasItems(bookResponse1.getTitle(), bookResponse2.getTitle())),
                            jsonPath("$.data.length()").value(2));
        }

        @Test
        @DisplayName("UTP-BC-R002: Should return response entity when read all books by category id")
        void shouldReturnResponseEntityWhenReadNewBookByCategoryId () throws Exception {

            // Arrange
            BookResponseDto bookResponse1 = BookTestData.sampleBookResponse();
            BookResponseDto bookResponse2 = BookTestData.sampleBookResponse2();

            final long testCategoryId = BookTestData.sampleBookEntity().getCategory().getId();

            List<BookResponseDto> expectedResponses = List.of(bookResponse1, bookResponse2);

            // Stub
            when(bookService.getAllBooksByCategoryId(testCategoryId))
                    .thenReturn(expectedResponses);

            String listOfJsonResponses = new ObjectMapper().writeValueAsString(expectedResponses);

            // Act + Assert
            mockMvc.perform(get("/api/categories/{id}/books", testCategoryId)
                        .with(httpBasic("acrexia", "dummy"))
                        .contentType("application/json")
                        .content(listOfJsonResponses))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath(".message")
                                    .value("Books by category id retrieved successfully!"),
                            jsonPath("$.data[*]").isArray(),
                            jsonPath("$.data[*].title",
                                    hasItems(bookResponse1.getTitle(), bookResponse2.getTitle())),
                            jsonPath("$.data.length()").value(2));
        }

        @Test
        @DisplayName("UTP-BC-R003: Should return response entity when read specific book")
        void shouldReturnResponseEntityWhenReadSpecificBook () throws Exception {

            // Arrange
            final long bookResponseId = bookResponseDto.getId();

            // Stub
            when(bookService.getBookById(bookResponseId))
                    .thenReturn(bookResponseDto);

            String bookJsonResponse = new ObjectMapper().writeValueAsString(bookResponseDto);

            // Act + Assert
            mockMvc.perform(get("/api/books/{id}", bookResponseId)
                        .with(httpBasic("acrexia", "dummy"))
                        .content(bookJsonResponse)
                        .contentType("application/json"))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.message")
                                    .value("Specific book retrieved successfully!"),
                            jsonPath("$.data.title").value(bookResponseDto.getTitle()));
        }

        @Test
        @DisplayName("UTP-BC-R003: Should return response entity when update specific book")
        void shouldReturnResponseEntityWhenUpdateSpecificBook () throws Exception {

            // Arrange
            BookUpdateDto bookUpdateDto = BookTestData.sampleBookUpdate();
            BookEntity testBook = BookTestData.sampleBookEntity();
            final long testBookId = testBook.getId();

            // Stub
            when(bookService.updateBook(testBookId, bookUpdateDto))
                    .thenReturn(bookResponseDto);

            String bookJsonUpdate = new ObjectMapper().writeValueAsString(bookUpdateDto);

            // Act + Assert
            mockMvc.perform(put("/api/books/{id}", testBookId)
                        .with(csrf())
                        .with(httpBasic("acrexia", "dummy"))
                        .contentType("application/json")
                        .content(bookJsonUpdate))
                    .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value("Specific book updated successfully!"));
        }

        @Test
        @DisplayName("UTP-BC-R003: Should return response entity when delete specific book")
        void shouldReturnResponseEntityWhenDeleteSpecificBook () throws Exception {

            // Arrange
            final long bookId = 1L;

            // Stub
            doNothing().when(bookService).deleteBookById(bookId);

            // Act + Assert
            mockMvc.perform(delete("/api/books/{id}", bookId)
                        .with(csrf())
                        .with(httpBasic("acrexia","dummy")))
                    .andExpect(status().isNoContent())
                    .andExpect(content().string(""));

            // Verify
            verify(bookService, times(1))
                    .deleteBookById(bookId);
        }
    }

    @Nested
    class NegativeTests {

        @Test
        @DisplayName("UTN-BC-C001: Should return 409 status in create category if name already exist")
        void shouldReturn409statusInCreateBookIfTitleAlreadyExist () throws Exception {

            // Arrange
            final String DUPLICATE_RESOURCE_FOUND = "Book title '%s' already exist!";
            String expectedMessage = String.format(DUPLICATE_RESOURCE_FOUND, bookRequestDto.getTitle());

            String bookJsonRequest = new ObjectMapper().writeValueAsString(bookRequestDto);

            // Stub
            when(bookService.saveBookData(any()))
                    .thenThrow(new DuplicateResourceFoundException
                            (String.format(DUPLICATE_RESOURCE_FOUND, bookRequestDto.getTitle())));

            mockMvc.perform(post("/api/books")
                        .with(csrf())
                        .with(httpBasic("acrexia", "dummy"))
                        .contentType("application/json")
                        .content(bookJsonRequest))
                    .andExpectAll(
                        status().isConflict(),
                        jsonPath("$.errorMessage")
                                .value(expectedMessage),
                        jsonPath("$.httpStatus").value(HttpStatus.CONFLICT.value()));
        }

        @Test
        @DisplayName("UTN-CC-R001: Should return 404 status if book not found")
        void shouldReturn404StatusIfBookNotFound () throws Exception {

            // Arrange
            final long nonExistentId = 99L;
            final String RESOURCE_NOT_FOUND = "This ID: %d doesn't exist!";
            String expectedErrorMessage = String.format(RESOURCE_NOT_FOUND, nonExistentId);

            // Stub
            when(bookService.getBookById(nonExistentId))
                    .thenThrow(new ResourceNotFoundException(expectedErrorMessage));

            // Act + Assert
            mockMvc.perform(get("/api/books/{id}", nonExistentId)
                        .with(httpBasic("acrexia", "dummy")))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.errorMessage").value(expectedErrorMessage),
                            jsonPath("$.httpStatus").value(HttpStatus.NOT_FOUND.value()));
        }

        @Test
        @DisplayName("UTN-CC-S001: Should return 400 status if book data has missing input")
        void shouldReturn400statusIfBookDataHasMissingInput () throws Exception {

            // Arrange
            bookRequestDto.setIsInStock(null);
            final String errorMessage = "Stock Status (isInStock) is Required!";

            String badJsonRequest = new ObjectMapper().writeValueAsString(bookRequestDto);

            // Act + Assert
            mockMvc.perform(post("/api/books")
                        .with(csrf())
                        .with(httpBasic("acrexia", "dummy"))
                        .contentType("application/json")
                        .content(badJsonRequest))
                    .andExpectAll(
                        status().isBadRequest(),
                            jsonPath("$.errorMessage").isString(),
                            jsonPath("$.errorMessage").value(containsString("isInStock")),
                        jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.value()));
        }

        @Test
        @DisplayName("UTN-CC-S002: Should return 400 status if book request has no json body")
        void shouldReturn400statusIfBookRequestHasNoJsonBody () throws Exception {

            // Act + Assert
            mockMvc.perform(post("/api/books")
                            .with(httpBasic("acrexia", "dummy"))
                            .with(csrf())
                            .content("")
                            .contentType("application/json"))
                    .andExpect(status().isBadRequest());
        }
    }
}
