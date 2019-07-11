package com.n26.presentation.transactions.time;

import com.n26.logic.exception.FutureTransactionException;
import com.n26.logic.exception.OutdatedTransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.n26.logic.configuration.StatisticsTimeConfiguration.TIME_WINDOW;

@Service
public class TimestampValidator {

    private TimeProvider timeProvider;

    @Autowired
    public TimestampValidator(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public void assertValid(Instant transactionTimestamp) {
        Instant currentTimestamp = timeProvider.now();
        if (currentTimestamp.isBefore(transactionTimestamp)) {
            String message = "Timestamp " + transactionTimestamp + " is future than " + currentTimestamp;
            throw new FutureTransactionException(message);
        }
        Instant validTimestamp = currentTimestamp.minusMillis(TIME_WINDOW);
        if (transactionTimestamp.isBefore(validTimestamp)) {
            String message = "Timestamp " + transactionTimestamp + " is older than " + validTimestamp;
            throw new OutdatedTransactionException(message);
        }
    }


}
