package com.n26.logic.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class Transaction implements Comparable<Transaction> {
    private BigDecimal amount;
    private Instant timestamp;

    @Override
    public int compareTo(Transaction that) {
        if (this == that) {
            return 0;
        }
        if (timestamp.isBefore(that.getTimestamp())) {
            return -1;
        }
        if (timestamp.isAfter(that.getTimestamp())) {
            return 1;
        }
        return 0;
    }
}
