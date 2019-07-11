package com.n26.logic.transactions.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY)
public class FutureTransactionException extends RuntimeException {

    private static final long serialVersionUID = 8238102219347016353L;

    public FutureTransactionException() {
    }

    public FutureTransactionException(String message) {
        super(message);
    }

    public FutureTransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
