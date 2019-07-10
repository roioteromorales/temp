package com.n26.presentation;


import com.n26.logic.exception.FutureTransactionException;
import com.n26.logic.exception.InvalidTransactionException;
import com.n26.logic.exception.OutdatedTransactionException;
import com.n26.logic.model.Transaction;
import com.n26.logic.services.TransactionService;
import com.n26.presentation.transactions.TransactionsController;
import com.n26.presentation.transactions.model.ApiTransaction;
import com.n26.presentation.transactions.model.TransactionsMapper;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionsControllerTest {

    public static final String INVALID_CONTENT = "INVALID CONTENT";

    @Mock
    private TransactionService transactionServiceMock;
    @Mock
    private TransactionsMapper transactionsMapperMock;

    @Before
    public void initialiseRestAssuredMockMvcStandalone() {
        RestAssuredMockMvc.standaloneSetup(new TransactionsController(transactionServiceMock, transactionsMapperMock));
    }

    @Test
    public void whenTransactionServiceSavesAValidTransaction_thenReturn201Status() {
        ApiTransaction transaction = new ApiTransaction();

        given()
                .body(transaction)
                .contentType(ContentType.JSON)
            .when()
                .post("/transactions")
            .then()
                .log()
                .body()
                .statusCode(201);
    }

    @Test
    public void whenTransactionServiceTriesToSaveAnOlderTransaction_thenReturn204Status() {
        ApiTransaction apiTransaction = new ApiTransaction();
        apiTransaction.setTimestamp("OLD");

        Transaction transaction = new Transaction();
        when(transactionsMapperMock.map(apiTransaction)).thenReturn(transaction);
        when(transactionServiceMock.save(transaction)).thenThrow(new OutdatedTransactionException());

        given()
                .body(apiTransaction)
                .contentType(ContentType.JSON)
            .when()
                .post("/transactions")
            .then()
                .log()
                .body()
                .statusCode(204);
    }

    @Test
    public void whenTransactionServiceTriesToSaveAnInvalidJson_thenReturn400Status() {
        given()
                .body(INVALID_CONTENT)
                .contentType(ContentType.JSON)
            .when()
                .post("/transactions")
            .then()
                .log()
                .body()
                .statusCode(400);
    }

    @Test
    public void whenTransactionServiceTriesToSaveTransactionWithFutureDate_thenReturn422Status() {
        ApiTransaction apiTransaction = new ApiTransaction();
        apiTransaction.setTimestamp("FUTURE");

        Transaction transaction = new Transaction();
        when(transactionsMapperMock.map(apiTransaction)).thenReturn(transaction);
        when(transactionServiceMock.save(transaction)).thenThrow(new FutureTransactionException());

        given()
                .body(apiTransaction)
                .contentType(ContentType.JSON)
            .when()
                .post("/transactions")
            .then()
                .log()
                .body()
                .statusCode(422);
    }

    @Test
    public void whenTransactionServiceTriesToSaveAnUnparsableTransaction_thenReturn422Status() {
        ApiTransaction apiTransaction = new ApiTransaction();
        apiTransaction.setTimestamp("UNPARSABLE");

        when(transactionsMapperMock.map(apiTransaction)).thenThrow(new InvalidTransactionException());

        given()
                .body(apiTransaction)
                .contentType(ContentType.JSON)
            .when()
                .post("/transactions")
            .then()
                .log()
                .body()
                .statusCode(422);
    }

    @Test
    public void whenTransactionServiceTriesToSaveTransactionWithNotParsableAmount_thenReturn422Status() {
        ApiTransaction apiTransaction = new ApiTransaction();
        apiTransaction.setAmount("UNPARSABLE");

        when(transactionsMapperMock.map(apiTransaction)).thenThrow(new InvalidTransactionException());

        given()
                .body(apiTransaction)
                .contentType(ContentType.JSON)
            .when()
                .post("/transactions")
            .then()
                .log()
                .body()
                .statusCode(422);
    }
}