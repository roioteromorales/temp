package com.n26.services;


import com.n26.logic.exception.OutdatedTransactionException;
import com.n26.logic.model.Transaction;
import com.n26.logic.services.BigDecimalSummaryStatistics;
import com.n26.logic.services.StatisticsService;
import com.n26.presentation.transactions.time.TimeProvider;
import com.n26.logic.services.TransactionService;
import com.n26.presentation.transactions.time.TimestampValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.Instant;

import static com.n26.logic.configuration.StatisticsTimeConfiguration.TIME_WINDOW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    private static final Instant NOW = Instant.ofEpochMilli(TIME_WINDOW * 2);
    private static final Instant OUTDATED = NOW.minusMillis(TIME_WINDOW + 1);

    @Mock
    private TimestampValidator timestampValidator;

    @Mock
    private StatisticsService statisticsServiceMock;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void whenSavingAValidTransaction_shouldReturnTheTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTimestamp(NOW);

        Transaction savedTransaction = transactionService.save(transaction);

        assertThat(savedTransaction).isEqualTo(transaction);
    }

    @Test
    public void whenSavingATransaction_isValidatedThroughTimeStampValidator() {
        Transaction outdatedTransaction = new Transaction();
        outdatedTransaction.setTimestamp(OUTDATED);

        transactionService.save(outdatedTransaction);

        verify(timestampValidator).assertValid(OUTDATED);
    }



}