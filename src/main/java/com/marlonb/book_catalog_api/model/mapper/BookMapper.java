package com.marlonb.book_catalog_api.model.mapper;

import com.marlonb.book_catalog_api.model.BookEntity;
import com.marlonb.book_catalog_api.model.dto.BookRequestDto;
import com.marlonb.book_catalog_api.model.dto.BookResponseDto;
import com.marlonb.book_catalog_api.model.dto.BookUpdateDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = CategoryMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface BookMapper {

    // BookRequestDto → BookEntity (for create operation)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "categoryId")
    BookEntity toEntity (BookRequestDto createBookRequest);

    // BookEntity → BookResponseDto (for read operation)
    BookResponseDto toResponse (BookEntity book);

    // List of BookEntity → BookResponseDto (for read operation)
    List<BookResponseDto> toResponse (List<BookEntity> listOfBooks);

    // void — update mapping (for update operation)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "categoryId")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity (BookUpdateDto dto, @MappingTarget BookEntity entity);
}
