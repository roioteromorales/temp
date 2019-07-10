package com.n26.logic.services;

import com.n26.logic.model.Statistic;
import com.n26.logic.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

    private BigDecimalSummaryStatistics bigDecimalSummaryStatistics;

    @Autowired
    public StatisticsService(BigDecimalSummaryStatistics bigDecimalSummaryStatistics) {
        this.bigDecimalSummaryStatistics = bigDecimalSummaryStatistics;
    }

    public Statistic getStatistics() {
        return bigDecimalSummaryStatistics.getStatistics();
    }

    public synchronized void add(Transaction transaction) { //TODO THINK IF ENOUGH WITH SYNCHRONIZED
        bigDecimalSummaryStatistics.accept(transaction.getAmount());
    }

    public void deleteAll() {
        bigDecimalSummaryStatistics.deleteAll();
    }
}


//BigDecimal a = new BigDecimal("10.12345");
//BigDecimal b = new BigDecimal("10.12556");
//
//a = a.setScale(2, BigDecimal.ROUND_HALF_EVEN);
//b = b.setScale(2, BigDecimal.ROUND_HALF_EVEN);
