package com.n26.logic.services;


import com.n26.logic.model.Statistic;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class BigDecimalSummaryStatisticsTest {

    private BigDecimalSummaryStatistics bigDecimalSummaryStatistics;

    @Before
    public void setUp() {
        bigDecimalSummaryStatistics = new BigDecimalSummaryStatistics();
    }

    @Test
    public void whenGettingDefaultStatistics_shouldContainDefaultValues() {
        Statistic statistics = bigDecimalSummaryStatistics.getStatistics();

        assertThat(statistics.getSum().longValue()).isEqualTo(0);
        assertThat(statistics.getMax().longValue()).isEqualTo(0);
        assertThat(statistics.getMin().longValue()).isEqualTo(0);
        assertThat(statistics.getAvg().longValue()).isEqualTo(0);
        assertThat(statistics.getCount()).isEqualTo(0);
    }

    @Test
    public void whenGettingStatisticsAfterOneInsert_shouldReturnExpectedStatistics() {
        bigDecimalSummaryStatistics.accept(new BigDecimal(12.3343));

        Statistic statistics = bigDecimalSummaryStatistics.getStatistics();

        assertThat(statistics.getSum().floatValue()).isEqualTo(12.3343f);
        assertThat(statistics.getMax().floatValue()).isEqualTo(12.3343f);
        assertThat(statistics.getMin().floatValue()).isEqualTo(12.3343f);
        assertThat(statistics.getAvg().floatValue()).isEqualTo(12.33f);
        assertThat(statistics.getCount()).isEqualTo(1);
    }

    @Test
    public void whenGettingStatisticsAfterTwoInserts_shouldReturnExpectedStatistics() {
        bigDecimalSummaryStatistics.accept(new BigDecimal(12.3343));
        bigDecimalSummaryStatistics.accept(new BigDecimal(11.1111));

        Statistic statistics = bigDecimalSummaryStatistics.getStatistics();

        assertThat(statistics.getSum().floatValue()).isEqualTo(23.4454f);
        assertThat(statistics.getMax().floatValue()).isEqualTo(12.3343f);
        assertThat(statistics.getMin().floatValue()).isEqualTo(11.1111f);
        assertThat(statistics.getAvg().floatValue()).isEqualTo(11.72f);
        assertThat(statistics.getCount()).isEqualTo(2);
    }
}