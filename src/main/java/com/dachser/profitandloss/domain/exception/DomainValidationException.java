package com.dachser.profitandloss.domain.exception;


public class DomainValidationException extends RuntimeException {
    public DomainValidationException(final String message) {
        super(message);
    }
}
