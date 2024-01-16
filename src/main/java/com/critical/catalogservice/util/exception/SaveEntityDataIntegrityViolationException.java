package com.critical.catalogservice.util.exception;

public class SaveEntityDataIntegrityViolationException extends RuntimeException
{
    public SaveEntityDataIntegrityViolationException(String message)
    {
        super(message);
    }
}