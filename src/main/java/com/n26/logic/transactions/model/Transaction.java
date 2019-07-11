package com.n26.logic.transactions.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class Transaction {
    private BigDecimal amount;
    private Instant timestamp;

}
