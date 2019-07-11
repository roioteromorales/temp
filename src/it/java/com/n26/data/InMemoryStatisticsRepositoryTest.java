package com.n26.data;


import com.n26.logic.model.Statistic;
import com.n26.logic.model.Transaction;
import com.n26.logic.repositories.StatisticsRepository;
import com.n26.logic.services.BigDecimalSummaryStatistics;
import com.n26.presentation.transactions.time.TimeProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryStatisticsRepositoryTest {

    private static final Instant NOW = Instant.ofEpochMilli(100000);
    private static final BigDecimal AMOUNT = new BigDecimal(12.345);
    private StatisticsRepository statisticsRepository;

    @Mock
    private TimeProvider timeProvider;

    @Before
    public void setUp() {
        statisticsRepository = new InMemoryStatisticsRepository(timeProvider);
        when(timeProvider.now()).thenReturn(NOW);
    }

    @Test
    public void whenRepositoryIsEmpty_shouldReturnEmptyStatistics() {
        Statistic statistics = statisticsRepository.getStatistics();

        assertThat(statistics).isEqualTo(Statistic.empty());
    }

    @Test
    public void whenAddingAValidTransactionToABucket_shouldAppearInTheStatistics() {
        Transaction transaction = aValidTransaction();
        statisticsRepository.add(transaction);

        Statistic statistics = statisticsRepository.getStatistics();

        assertThat(statistics).isEqualTo(statisticsFrom(transaction));
    }

    @Test
    public void whenAddingTwoValidTransactionsToABucket_shouldAggregateThemInTheStatistics() {
        Transaction transaction = aValidTransaction();
        Transaction transaction2 = aValidTransaction();
        statisticsRepository.add(transaction);
        statisticsRepository.add(transaction2);

        Statistic statistics = statisticsRepository.getStatistics();

        assertThat(statistics).isEqualTo(statisticsFrom(transaction,transaction2));
    }

    @Test
    public void whenAddingAnOutdatedTransactionToABucket_shouldNotAppearInTheStatistics() {
        Transaction transaction = anOutdatedTransaction();
        statisticsRepository.add(transaction);

        Statistic statistics = statisticsRepository.getStatistics();

        assertThat(statistics).isEqualTo(Statistic.empty());
    }

    @Test
    public void whenAddingAValidTransactionToABucket_thenBecomesOutdated_shouldNotAppearInTheStatistics() {
        Transaction transaction = aValidTransaction();
        statisticsRepository.add(transaction);

        assertThat(statisticsRepository.getStatistics()).isEqualTo(statisticsFrom(transaction));

        when(timeProvider.now()).thenReturn(NOW.plusSeconds(65));

        assertThat(statisticsRepository.getStatistics()).isEqualTo(Statistic.empty());
    }

    private Transaction aValidTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTimestamp(NOW);
        transaction.setAmount(AMOUNT);
        return transaction;
    }

    private Transaction anOutdatedTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTimestamp(NOW.minusSeconds(61));
        transaction.setAmount(AMOUNT);
        return transaction;
    }

    private Statistic statisticsFrom(Transaction... transactions) {
        BigDecimalSummaryStatistics result = new BigDecimalSummaryStatistics();
        for (Transaction t : transactions){
            result.accept(t.getAmount());
        }
        return result.getStatistics();
    }
}