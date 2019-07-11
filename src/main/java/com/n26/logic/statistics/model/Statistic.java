package com.n26.logic.statistics.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Statistic {
    private BigDecimal sum;
    private BigDecimal avg;
    private BigDecimal max;
    private BigDecimal min;
    private Long count;

    public static Statistic empty() {
        Statistic statistic = new Statistic();
        statistic.setCount(0L);
        statistic.setSum(BigDecimal.ZERO);
        statistic.setAvg(BigDecimal.ZERO);
        statistic.setMin(BigDecimal.ZERO);
        statistic.setMax(BigDecimal.ZERO);
        return statistic;
    }
}
