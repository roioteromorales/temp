package com.n26.logic.statistics.repositories;

import com.n26.logic.statistics.model.Statistic;
import com.n26.logic.transactions.model.Transaction;

public interface StatisticsRepository {
    void add(Transaction transaction);

    Statistic getStatistics();

    void resetAll();
}
