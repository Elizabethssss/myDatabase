package com.valteris.database.exception;

public class DatabaseNotFoundException extends RuntimeException{

    public DatabaseNotFoundException(String message) {
        super(message);
    }
}
