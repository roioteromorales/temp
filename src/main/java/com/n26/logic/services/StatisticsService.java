package com.n26.logic.services;

import com.n26.logic.model.Statistic;
import com.n26.logic.model.Transaction;
import com.n26.logic.repositories.StatisticsRepository;
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