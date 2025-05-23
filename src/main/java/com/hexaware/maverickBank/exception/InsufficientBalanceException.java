package com.hexaware.maverickBank.exception;

public class InsufficientBalanceException extends RuntimeException { // Or Exception, depending on your design

    private static final long serialVersionUID = 1L; // You can assign any long value here

    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientBalanceException(Throwable cause) {
        super(cause);
    }
}