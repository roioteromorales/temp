package com.n26.logic.statistics;


import com.n26.logic.statistics.BigDecimalSummaryStatistics;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class BigDecimalSummaryStatisticsTest {

    private BigDecimalSummaryStatistics bigDecimalSummaryStatistics;

    @Before
    public void setUp() {
        bigDecimalSummaryStatistics = new BigDecimalSummaryStatistics();
    }

    @Test
    public void whenGettingDefaultStatistics_shouldContainDefaultValues() {
        assertThat(bigDecimalSummaryStatistics.getSum().longValue()).isEqualTo(0);
        assertThat(bigDecimalSummaryStatistics.getMax().longValue()).isEqualTo(0);
        assertThat(bigDecimalSummaryStatistics.getMin().longValue()).isEqualTo(0);
        assertThat(bigDecimalSummaryStatistics.getAvg().longValue()).isEqualTo(0);
        assertThat(bigDecimalSummaryStatistics.getCount()).isEqualTo(0);
    }

    @Test
    public void whenGettingStatisticsAfterOneInsert_shouldReturnExpectedStatistics() {
        bigDecimalSummaryStatistics.accept(new BigDecimal(12.3343));

        assertThat(bigDecimalSummaryStatistics.getSum().floatValue()).isEqualTo(12.3343f);
        assertThat(bigDecimalSummaryStatistics.getMax().floatValue()).isEqualTo(12.3343f);
        assertThat(bigDecimalSummaryStatistics.getMin().floatValue()).isEqualTo(12.3343f);
        assertThat(bigDecimalSummaryStatistics.getAvg().floatValue()).isEqualTo(12.33f);
        assertThat(bigDecimalSummaryStatistics.getCount()).isEqualTo(1);
    }

    @Test
    public void whenGettingStatisticsAfterTwoInserts_shouldReturnExpectedStatistics() {
        bigDecimalSummaryStatistics.accept(new BigDecimal(12.3343));
        bigDecimalSummaryStatistics.accept(new BigDecimal(11.1111));

        assertThat(bigDecimalSummaryStatistics.getSum().floatValue()).isEqualTo(23.4454f);
        assertThat(bigDecimalSummaryStatistics.getMax().floatValue()).isEqualTo(12.3343f);
        assertThat(bigDecimalSummaryStatistics.getMin().floatValue()).isEqualTo(11.1111f);
        assertThat(bigDecimalSummaryStatistics.getAvg().floatValue()).isEqualTo(11.72f);
        assertThat(bigDecimalSummaryStatistics.getCount()).isEqualTo(2);
    }
}