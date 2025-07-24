package com.marlonb.book_catalog_api.exception.custom;

public class DataAccessException extends RuntimeException {
    public DataAccessException (String message) {
        super(message);
    }
}
