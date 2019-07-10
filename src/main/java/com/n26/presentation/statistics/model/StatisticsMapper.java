package com.n26.presentation.statistics.model;

import com.n26.logic.model.Statistic;
import org.springframework.stereotype.Service;

import static java.math.BigDecimal.ROUND_HALF_UP;

@Service
public class StatisticsMapper {

    private static final int SCALE = 2;

    public ApiStatistic map(Statistic statistics) {
        ApiStatistic apiStatistic = new ApiStatistic();
        apiStatistic.setAvg(statistics.getAvg().setScale(SCALE, ROUND_HALF_UP).toString());
        apiStatistic.setCount(statistics.getCount());
        apiStatistic.setMax(statistics.getMax().setScale(2, ROUND_HALF_UP).toString());
        apiStatistic.setMin(statistics.getMin().setScale(2, ROUND_HALF_UP).toString());
        apiStatistic.setSum(statistics.getSum().setScale(2, ROUND_HALF_UP).toString());
        return apiStatistic;
    }
}
