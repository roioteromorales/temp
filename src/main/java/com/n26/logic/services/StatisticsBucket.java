package com.n26.logic.services;

import com.n26.logic.model.Statistic;

import java.math.BigDecimal;
import java.time.Instant;

public class StatisticsBucket {
    private Instant bucketTime;
    private BigDecimalSummaryStatistics statistics = new BigDecimalSummaryStatistics();

    public StatisticsBucket(Instant bucketTime) {
        this.bucketTime = bucketTime;
    }

    public void add(BigDecimal amount) {
        statistics.accept(amount);
    }

    public StatisticsBucket merge(StatisticsBucket statisticsBucket){
        statistics = statistics.merge(statisticsBucket.statistics);
        return this;
    }
    public void reset() {
        statistics.reset();
    }

    public Instant getTime() {
        return bucketTime;
    }

    public Statistic getStatistics() {
        return statistics.getStatistics();
    }
}
