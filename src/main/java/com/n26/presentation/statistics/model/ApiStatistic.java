package com.n26.presentation.statistics.model;

import lombok.Data;

@Data
public class ApiStatistic {
    private String sum;
    private String avg;
    private String max;
    private String min;
    private Long count;

    public static ApiStatistic empty() {
        ApiStatistic statistic = new ApiStatistic();
        statistic.setCount(0L);
        statistic.setSum("0");
        statistic.setAvg("0");
        statistic.setMin("0");
        statistic.setMax("0");
        return statistic;
    }
}
