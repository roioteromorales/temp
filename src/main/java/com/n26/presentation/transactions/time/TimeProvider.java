package com.n26.presentation.transactions.time;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TimeProvider {

    public Instant getCurrentTimestamp() {
        return Instant.now().truncatedTo( ChronoUnit.MILLIS );
    }
}
