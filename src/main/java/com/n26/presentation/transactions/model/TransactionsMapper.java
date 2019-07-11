package com.n26.presentation.transactions.model;

import com.n26.logic.transactions.exception.InvalidTransactionException;
import com.n26.logic.transactions.model.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeParseException;

@Service
public class TransactionsMapper {

    public Transaction map(ApiTransaction apiTransaction) {
        Transaction transaction = new Transaction();
        transaction.setTimestamp(parseTimestamp(apiTransaction.getTimestamp()));
        transaction.setAmount(parseAmount(apiTransaction.getAmount()));
        return transaction;
    }

    private Instant parseTimestamp(String inputTimestamp) {
        try {
            return Instant.parse(inputTimestamp);
        } catch (DateTimeParseException e) {
            throw new InvalidTransactionException("Transaction timestamp is not parsable: " + inputTimestamp, e);
        }
    }

    private BigDecimal parseAmount(String amount) {
        try {
            return new BigDecimal(amount);
        } catch (NumberFormatException e) {
            throw new InvalidTransactionException("Transaction amount is not parsable to BigDecimal: " + amount, e);
        }
    }
}
