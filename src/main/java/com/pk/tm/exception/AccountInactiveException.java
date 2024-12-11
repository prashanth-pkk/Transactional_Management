package com.pk.tm.exception;

public class AccountInactiveException extends RuntimeException{
    public AccountInactiveException(String message){
        super(message);
    }

    public AccountInactiveException(String message, Throwable cause){
        super(message,cause);
    }
}
