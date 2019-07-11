package com.n26.presentation.transactions.time;

import com.n26.logic.transactions.exception.FutureTransactionException;
import com.n26.logic.transactions.exception.OutdatedTransactionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;

import static com.n26.presentation.transactions.time.TimestampValidator.TIME_WINDOW;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TimestampValidatorTest {

    private static final Instant NOW = Instant.ofEpochSecond(100);

    @Mock
    private TimeProvider timeProvider;

    @InjectMocks
    private TimestampValidator timestampValidator;

    @Before
    public void setUp() {
        when(timeProvider.now()).thenReturn(NOW);
    }

    @Test
    public void whenInstantIsCorrect_shouldSuccess() {
        timestampValidator.assertValid(NOW);
    }


    @Test
    public void whenInstantIsFuture_shouldThrowFutureTransactionException() {
        assertThatExceptionOfType(FutureTransactionException.class)
                .isThrownBy(() -> timestampValidator.assertValid(NOW.plusSeconds(1)))
                .withMessageContaining("is future than")
                .withNoCause();
    }

    @Test
    public void whenInstantIsOld_shouldThrowOutdatedTransactionException() {
        assertThatExceptionOfType(OutdatedTransactionException.class)
                .isThrownBy(() -> timestampValidator.assertValid(NOW.minusMillis(TIME_WINDOW * 2)))
                .withMessageContaining("is older than")
                .withNoCause();
    }
}