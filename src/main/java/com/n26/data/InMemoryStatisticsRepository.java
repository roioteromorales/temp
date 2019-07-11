package com.n26.data;

import com.n26.logic.model.Statistic;
import com.n26.logic.model.Transaction;
import com.n26.logic.repositories.StatisticsRepository;
import com.n26.logic.services.StatisticsBucket;
import com.n26.presentation.transactions.time.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public class InMemoryStatisticsRepository implements StatisticsRepository {

    private static final int MILLISECONDS_PER_BUCKET = 1000;
    private static final int MILLISECONDS_TO_KEEP = 60000;
    private static final int BUCKETS = MILLISECONDS_TO_KEEP / MILLISECONDS_PER_BUCKET;

    private StatisticsBucket[] buckets = new StatisticsBucket[BUCKETS];

    private TimeProvider timeProvider;

    @Autowired
    public InMemoryStatisticsRepository(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public void add(Transaction transaction) {
        StatisticsBucket statistics = getStatisticsBucketFor(transaction.getTimestamp());
        statistics.add(transaction.getAmount());
    }

    @Override
    public Statistic getStatistics() {
        StatisticsBucket total = new StatisticsBucket(now());
        for (StatisticsBucket bucket : buckets) {
            if (bucket != null) {
                if (isOutdated(bucket.getTime())) {
                    bucket.reset();
                }
                total.merge(bucket);
            }
        }
        return total.getStatistics();
    }

    @Override
    public void resetAll() {//todo deal with synchronization
        buckets = new StatisticsBucket[BUCKETS];
    }

    private StatisticsBucket getStatisticsBucketFor(Instant timestamp) {
        StatisticsBucket bucket = buckets[getBucketFor(timestamp)];
        if (bucket == null) {
            bucket = new StatisticsBucket(timestamp);
            buckets[getBucketFor(timestamp)] = bucket;
        }

        if (isOutdated(bucket.getTime())) {
            bucket.reset();
        }

        return bucket;
    }

    private boolean isOutdated(Instant bucketTime) {
        return (bucketTime.isBefore(now().minusMillis(MILLISECONDS_TO_KEEP)));
    }

    private Instant now() {
        return timeProvider.getCurrentTimestamp();
    }

    private int getBucketFor(Instant timestamp) {
        return (int) (bucketTime(timestamp) % BUCKETS);
    }

    private long bucketTime(Instant timestamp) {
        return timestamp.toEpochMilli() / MILLISECONDS_PER_BUCKET;
    }
}
