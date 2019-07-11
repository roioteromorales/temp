package com.n26;

import com.n26.data.InMemoryStatisticsRepository;
import com.n26.logic.model.Statistic;
import com.n26.logic.model.Transaction;
import com.n26.logic.repositories.StatisticsRepository;
import com.n26.presentation.transactions.time.TimeProvider;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static com.n26.helpers.TransactionsTestHelper.NOW;
import static com.n26.helpers.TransactionsTestHelper.aValidTransaction;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * This class contains a test to check the concurrency when adding transactions.
 * In the InMemoryStatisticsRepository in the method add(transaction), there is a lock mechanism to avoid that condition
 *
 * There is another race condition when having to reset the bucket because is old. That condition is not tested.
 * Could be tested injecting some code into the production code.
 *
 * As well could redesign the repository to be able to use a library like Thread Weaver to test it correctly
 * https://code.google.com/archive/p/thread-weaver/wikis/UsersGuide.wiki
 */
@RunWith(MockitoJUnitRunner.class)
public class ConcurrencyTest {

    private static final int TOTAL = 50000;

    @Mock
    private TimeProvider timeProvider;

    private StatisticsRepository statisticsRepository;

    @Before
    public void setUp() {
        statisticsRepository = new InMemoryStatisticsRepository(timeProvider);
        when(timeProvider.now()).thenReturn(NOW);
    }


    @Ignore
    @Test
    public void whenAddingMultipleTransactions_shouldBeConcurrentSafe() {
        Transaction transaction = aValidTransaction();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < TOTAL; i++) {
            threads.add(new Thread(() -> statisticsRepository.add(transaction)));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Statistic statistics = statisticsRepository.getStatistics();
        assertThat(statistics.getCount()).isEqualTo(TOTAL);
    }

}
