package com.n26.logic.repositories;

import com.n26.logic.model.Statistic;
import com.n26.logic.model.Transaction;

public interface StatisticsRepository {
    void add(Transaction transaction);

    Statistic getStatistics();

    void resetAll();
}
