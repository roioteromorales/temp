package com.n26.presentation.statistics.model;

import com.n26.logic.statistics.model.Statistic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static com.n26.helpers.StatisticsTestHelper.aValidStatistic;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsMapperTest {

    @InjectMocks
    private StatisticsMapper statisticsMapper;

    @Test
    public void whenMappingStatistics_shouldReturnHalfRoundUpApiStatistics() {
        Statistic statistic = aValidStatistic();
        statistic.setMax(new BigDecimal(1.009));
        statistic.setMin(new BigDecimal(1.001));
        statistic.setAvg(new BigDecimal(1.001));
        statistic.setSum(new BigDecimal(1.009));

        ApiStatistic apiStatistic = statisticsMapper.map(statistic);

        assertThat(apiStatistic.getMax()).isEqualTo("1.01");
        assertThat(apiStatistic.getMin()).isEqualTo("1.00");
        assertThat(apiStatistic.getAvg()).isEqualTo("1.00");
        assertThat(apiStatistic.getSum()).isEqualTo("1.01");
    }
}