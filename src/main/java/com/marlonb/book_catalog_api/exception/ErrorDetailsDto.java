package com.marlonb.book_catalog_api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDetailsDto {

    private LocalDateTime timeStamp;
    private int httpStatus;
    private String errorMessage;
}
