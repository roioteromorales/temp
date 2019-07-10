package com.n26.presentation;

import com.n26.logic.model.Statistic;
import com.n26.logic.services.StatisticsService;
import com.n26.presentation.statistics.StatisticsController;
import com.n26.presentation.statistics.model.ApiStatistic;
import com.n26.presentation.statistics.model.StatisticsMapper;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;


@RunWith(MockitoJUnitRunner.class)
public class StatisticsControllerTest {

    @Mock
    private StatisticsService statisticsServiceMock;
    @Mock
    private StatisticsMapper statisticsMapperMock;

    @Before
    public void initialiseRestAssuredMockMvcStandalone() {
        RestAssuredMockMvc.standaloneSetup(new StatisticsController(statisticsServiceMock, statisticsMapperMock));
    }

    @Test
    public void whenRetrievingStatistics_returnValidStatisticsJson() {
        Statistic statistic = Statistic.empty();
        when(statisticsServiceMock.getStatistics()).thenReturn(statistic);

        ApiStatistic apiStatistic = ApiStatistic.empty();
        when(statisticsMapperMock.map(statistic)).thenReturn(apiStatistic);


        ApiStatistic returnedStatistic = given()
                .when()
                .get("/statistics")
                .then()
                .assertThat()
                .statusCode(OK.value())
                .contentType(ContentType.JSON)
                .extract().as(ApiStatistic.class);

        assertThat(returnedStatistic).isEqualTo(apiStatistic);
    }
}