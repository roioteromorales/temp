package com.n26.data;


import com.n26.logic.model.Statistic;
import com.n26.logic.model.Transaction;
import com.n26.logic.repositories.StatisticsRepository;
import com.n26.presentation.transactions.time.TimeProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.n26.helpers.StatisticsTestHelper.statisticsFrom;
import static com.n26.helpers.TransactionsTestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryStatisticsRepositoryTest {

    @Mock
    private TimeProvider timeProvider;

    private StatisticsRepository statisticsRepository;

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

        assertThat(statistics).isEqualTo(statisticsFrom(transaction, transaction2));
    }

    @Test
    public void whenAddingAnOutdatedTransactionToABucket_shouldReturnEmptyStatistics() {
        Transaction transaction = anOutdatedTransaction();
        statisticsRepository.add(transaction);

        Statistic statistics = statisticsRepository.getStatistics();

        assertThat(statistics).isEqualTo(Statistic.empty());
    }

    @Test
    public void whenAddingAValidTransactionToABucket_thenBecomesOutdated_shouldReturnEmptyStatistics() {
        Transaction transaction = aValidTransaction();
        statisticsRepository.add(transaction);

        assertThat(statisticsRepository.getStatistics()).isEqualTo(statisticsFrom(transaction));

        when(timeProvider.now()).thenReturn(NOW.plusSeconds(65));

        assertThat(statisticsRepository.getStatistics()).isEqualTo(Statistic.empty());
    }

    @Test
    public void whenAddingTransactions_thenResettingAll_shouldReturnEmptyStatistics() {
        Transaction transaction = aValidTransaction();
        statisticsRepository.add(transaction);

        statisticsRepository.resetAll();
        Statistic statistics = statisticsRepository.getStatistics();

        assertThat(statistics).isEqualTo(Statistic.empty());
    }


}