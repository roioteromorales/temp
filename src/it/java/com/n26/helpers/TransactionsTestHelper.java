package com.n26.helpers;

import com.n26.logic.transactions.model.Transaction;

import java.math.BigDecimal;
import java.time.Instant;

public class TransactionsTestHelper {
    public static final Instant NOW = Instant.ofEpochMilli(100000);
    public static final BigDecimal AMOUNT = new BigDecimal(12.345);

    public static Transaction aValidTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTimestamp(NOW);
        transaction.setAmount(AMOUNT);
        return transaction;
    }

    public static Transaction anOutdatedTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTimestamp(NOW.minusSeconds(61));
        transaction.setAmount(AMOUNT);
        return transaction;
    }

}
