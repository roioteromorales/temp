package com.n26.presentation.statistics;

import com.n26.logic.statistics.services.StatisticsService;
import com.n26.presentation.statistics.model.ApiStatistic;
import com.n26.presentation.statistics.model.StatisticsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    private StatisticsService statisticsService;
    private StatisticsMapper statisticsMapper;

    @Autowired
    public StatisticsController(StatisticsService statisticsService, StatisticsMapper statisticsMapper) {
        this.statisticsService = statisticsService;
        this.statisticsMapper = statisticsMapper;
    }

    @GetMapping("/statistics")
    public ApiStatistic getStatistics() {
        return statisticsMapper.map(statisticsService.getStatistics());
    }
}
