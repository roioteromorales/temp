package com.n26.presentation;

import com.fasterxml.jackson.core.JsonParseException;
import com.n26.logic.transactions.exception.FutureTransactionException;
import com.n26.logic.transactions.exception.InvalidTransactionException;
import com.n26.logic.transactions.exception.OutdatedTransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity handleJsonFormatException(Exception ex) {
        logger.debug(ex.getLocalizedMessage());
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OutdatedTransactionException.class)
    public ResponseEntity handleOutdatedTransaction(Exception ex) {
        logger.debug(ex.getLocalizedMessage());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler({FutureTransactionException.class, InvalidTransactionException.class})
    public ResponseEntity handleUnprocessableEntity(Exception ex) {
        logger.debug(ex.getLocalizedMessage());
        return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
