package com.n26.presentation.transactions.model;

import com.n26.logic.transactions.exception.InvalidTransactionException;
import com.n26.logic.transactions.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(MockitoJUnitRunner.class)

public class TransactionsMapperTest {

    @InjectMocks
    private TransactionsMapper transactionsMapper;
    private Instant NOW;
    private BigDecimal AMOUNT;

    @Before
    public void setUp() {
        NOW = Instant.ofEpochSecond(100);
        AMOUNT = new BigDecimal(12.345);
    }

    @Test
    public void whenParsingAValidApiTransaction_shouldReturnCorrectTransaction() {
        ApiTransaction apiTransaction = new ApiTransaction();
        apiTransaction.setTimestamp(NOW.toString());
        apiTransaction.setAmount("12.34");

        Transaction transaction = transactionsMapper.map(apiTransaction);

        assertThat(transaction.getTimestamp()).isEqualTo(NOW);
        assertThat(transaction.getAmount()).isEqualTo(AMOUNT.setScale(2, RoundingMode.DOWN));
    }

    @Test
    public void whenParsingAnApiTransactionWithInvalidTimestamp_shouldThrowInvalidTransactionException() {
        ApiTransaction apiTransaction = new ApiTransaction();
        apiTransaction.setTimestamp("INVALID_TIMESTAMP");
        apiTransaction.setAmount("12.34");

        assertThatExceptionOfType(InvalidTransactionException.class)
                .isThrownBy(() -> transactionsMapper.map(apiTransaction))
                .withMessageContaining("Transaction timestamp is not parsable")
                .withCauseInstanceOf(DateTimeParseException.class);
    }

    @Test
    public void whenParsingAnApiTransactionWithInvalidAmount_shouldThrowInvalidTransactionException() {
        ApiTransaction apiTransaction = new ApiTransaction();
        apiTransaction.setTimestamp(NOW.toString());
        apiTransaction.setAmount("INVALID_AMOUNT");

        assertThatExceptionOfType(InvalidTransactionException.class)
                .isThrownBy(() -> transactionsMapper.map(apiTransaction))
                .withMessageContaining("Transaction amount is not parsable to BigDecimal")
                .withCauseInstanceOf(NumberFormatException.class);
    }
}