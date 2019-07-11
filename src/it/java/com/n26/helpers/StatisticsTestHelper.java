package com.n26.helpers;

import com.n26.logic.statistics.model.Statistic;
import com.n26.logic.transactions.model.Transaction;
import com.n26.logic.statistics.BigDecimalSummaryStatistics;

public class StatisticsTestHelper {

    public static Statistic statisticsFrom(Transaction... transactions) {
        BigDecimalSummaryStatistics result = new BigDecimalSummaryStatistics();
        for (Transaction t : transactions) {
            result.accept(t.getAmount());
        }
        return result.getStatistics();
    }
}
