package com.critical.catalogservice.util.exception;

public class EntityNullException extends RuntimeException
{
    public EntityNullException(String message){
        super(message);
    }
}