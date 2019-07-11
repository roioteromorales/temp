package com.n26.data;

import com.n26.logic.model.Statistic;
import com.n26.logic.model.Transaction;
import com.n26.logic.repositories.StatisticsRepository;
import com.n26.logic.services.StatisticsBucket;
import com.n26.presentation.transactions.time.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Repository
public class InMemoryStatisticsRepository implements StatisticsRepository {

    private static final int MILLISECONDS_PER_BUCKET = 1000;
    private static final int MILLISECONDS_TO_KEEP = 60000;
    private static final int BUCKETS = MILLISECONDS_TO_KEEP / MILLISECONDS_PER_BUCKET;

    private StatisticsBucket[] buckets = new StatisticsBucket[BUCKETS];
    private Lock[] locks = new ReentrantLock[BUCKETS];
    private TimeProvider timeProvider;

    @Autowired
    public InMemoryStatisticsRepository(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public void add(Transaction transaction) {
        int bucketIndex = getBucketIndexFor(transaction.getTimestamp());
        lock(bucketIndex);
        try {
            StatisticsBucket bucket = getBucketFor(transaction.getTimestamp());
            bucket.add(transaction.getAmount());
        } finally {
            unlock(bucketIndex);
        }
    }

    @Override
    public Statistic getStatistics() {
        StatisticsBucket total = new StatisticsBucket(timeProvider.now());
        for (int bucketIndex = 0; bucketIndex < buckets.length; bucketIndex++) {
            StatisticsBucket bucket = buckets[bucketIndex];
            if (bucket != null) {
                resetIfOutdated(bucketIndex, bucket);
                total.merge(bucket);
            }
        }
        return total.getStatistics();
    }

    @Override
    public void resetAll() {
        buckets = new StatisticsBucket[BUCKETS];
    }

    private void resetIfOutdated(int bucketIndex, StatisticsBucket bucket) {
        lock(bucketIndex);
        try {
            if (isOutdated(bucket.getTimestamp())) {
                bucket.reset();
            }
        } finally {
            unlock(bucketIndex);
        }
    }

    private StatisticsBucket getBucketFor(Instant timestamp) {
        StatisticsBucket bucket = getStatisticsBucketOrDefault(timestamp);
        if (isOutdated(bucket.getTimestamp())) {
            bucket.reset();
        }
        return bucket;
    }

    private StatisticsBucket getStatisticsBucketOrDefault(Instant timestamp) {
        int bucketIndex = getBucketIndexFor(timestamp);
        if (buckets[bucketIndex] == null) {
            buckets[bucketIndex] = new StatisticsBucket(timestamp);
        }
        return buckets[bucketIndex];
    }

    private Lock getLockOrDefault(int bucketIndex) {
        if (locks[bucketIndex] == null) {
            locks[bucketIndex] = new ReentrantLock();
        }
        return locks[bucketIndex];
    }

    private void unlock(int bucketIndex) {
        getLockOrDefault(bucketIndex).unlock();
    }

    private void lock(int bucketIndex) {
        getLockOrDefault(bucketIndex).lock();
    }

    private boolean isOutdated(Instant bucketTime) {
        return (bucketTime.isBefore(timeProvider.now().minusMillis(MILLISECONDS_TO_KEEP)));
    }

    private int getBucketIndexFor(Instant timestamp) {
        return (int) (truncateTimestamp(timestamp) % BUCKETS);
    }

    private long truncateTimestamp(Instant timestamp) {
        return timestamp.toEpochMilli() / MILLISECONDS_PER_BUCKET;
    }
}
