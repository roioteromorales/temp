package com.n26.logic.statistics.services;

import com.n26.logic.statistics.model.Statistic;
import com.n26.logic.transactions.model.Transaction;
import com.n26.logic.statistics.repositories.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

    private StatisticsRepository statisticsRepository;

    @Autowired
    public StatisticsService(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    public Statistic getStatistics() {
        return statisticsRepository.getStatistics();
    }

    public void add(Transaction transaction) {
        statisticsRepository.add(transaction);
    }

    public void deleteAll() {
        statisticsRepository.resetAll();
    }
}