package com.marlonb.book_catalog_api.exception.custom;

public class DuplicateResourceFoundException extends RuntimeException{
    public DuplicateResourceFoundException (String message) {
        super(message);
    }
}
