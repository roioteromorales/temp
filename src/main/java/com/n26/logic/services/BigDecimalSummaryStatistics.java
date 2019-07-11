package com.n26.logic.services;

import com.n26.logic.model.Statistic;

import java.math.BigDecimal;
import java.util.function.Consumer;


public class BigDecimalSummaryStatistics implements Consumer<BigDecimal> {
    private static final BigDecimal MAX_VALUE = BigDecimal.valueOf(Long.MAX_VALUE);
    private static final BigDecimal MIN_VALUE = BigDecimal.valueOf(Long.MIN_VALUE);

    private BigDecimal count;
    private BigDecimal sum;
    private BigDecimal max;
    private BigDecimal min;

    public BigDecimalSummaryStatistics() {
        reset();
    }

    @Override
    public void accept(BigDecimal bigDecimal) {
        max = maximum(max, bigDecimal);
        min = minimum(min, bigDecimal);
        sum = sum.add(bigDecimal);
        count = count.add(BigDecimal.ONE);
    }

    public void reset() {
        count = BigDecimal.ZERO;
        sum = BigDecimal.ZERO;
        max = MIN_VALUE;
        min = MAX_VALUE;
    }

    public long getCount() {
        return count.longValue();
    }

    public BigDecimal getSum() {
        return sum;
    }

    public BigDecimal getMax() {
        if (max.equals(MIN_VALUE)) {
            return BigDecimal.ZERO;
        }
        return max;
    }

    public BigDecimal getMin() {
        if (min.equals(MAX_VALUE)) {
            return BigDecimal.ZERO;
        }
        return min;
    }

    public BigDecimal getAvg() {
        if (count.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        return sum.divide(count, 2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimalSummaryStatistics merge(BigDecimalSummaryStatistics statistics) {
        count = count.add(statistics.count);
        sum = sum.add(statistics.sum);
        max = maximum(max, statistics.max);
        min = minimum(min, statistics.min);
        return this;
    }

    public Statistic getStatistics() {
        Statistic statistic = new Statistic();
        statistic.setCount(getCount());
        statistic.setSum(getSum());
        statistic.setAvg(getAvg());
        statistic.setMax(getMax());
        statistic.setMin(getMin());
        return statistic;
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
