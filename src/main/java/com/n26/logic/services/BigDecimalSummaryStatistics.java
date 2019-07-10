package com.n26.logic.services;

import com.n26.logic.model.Statistic;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.function.Consumer;

import static com.n26.logic.configuration.StatisticsTimeConfiguration.TIME_WINDOW;


@Service
public class BigDecimalSummaryStatistics implements Consumer<BigDecimal> {
    private int timeWindow = TIME_WINDOW;
    private int timeGranularity = 1000;

    private BigDecimal count;
    private BigDecimal sum;
    private BigDecimal max;
    private BigDecimal min;

    public BigDecimalSummaryStatistics() {
        clean();
    }

    @Override
    public void accept(BigDecimal bigDecimal) {
        if (count.equals(BigDecimal.ZERO)) {
            max = bigDecimal;
            min = bigDecimal;
        } else {
            max = maximum(max, bigDecimal);
            min = minimum(min, bigDecimal);
        }
        sum = sum.add(bigDecimal);
        count = count.add(BigDecimal.ONE);
    }

    public Statistic getStatistics() {
        Statistic statistic = new Statistic();
        statistic.setCount(count.longValue());
        statistic.setSum(sum);
        statistic.setAvg(calculateAvg());
        statistic.setMax(max);
        statistic.setMin(min);
        return statistic;
    }

    public void deleteAll() {
        clean();
    }

    private void clean() {
        count = BigDecimal.ZERO;
        sum = BigDecimal.ZERO;
        max = BigDecimal.ZERO;
        min = BigDecimal.ZERO;
    }

    private BigDecimal calculateAvg() {
        if (count.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        return sum.divide(count, 2, BigDecimal.ROUND_HALF_UP);
    }

    private static BigDecimal maximum(BigDecimal max, BigDecimal input) {
        if (input.compareTo(max) > 0) {
            return input;
        }
        return max;
    }

    private static BigDecimal minimum(BigDecimal min, BigDecimal input) {
        if (input.compareTo(min) < 0) {
            return input;
        }
        return min;
    }

}
