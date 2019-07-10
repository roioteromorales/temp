package com.n26.logic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NO_CONTENT)
public class OutdatedTransactionException extends RuntimeException {

    private static final long serialVersionUID = 1854813352943435081L;

    public OutdatedTransactionException() {
    }

    public OutdatedTransactionException(String message) {
        super(message);
    }

    public OutdatedTransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
