package com.n26.presentation.transactions;


import com.n26.logic.services.TransactionService;
import com.n26.presentation.transactions.model.ApiTransaction;
import com.n26.presentation.transactions.model.TransactionsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/transactions")
public class TransactionsController {

    private TransactionService transactionService;
    private TransactionsMapper transactionsMapper;

    @Autowired
    public TransactionsController(TransactionService transactionService, TransactionsMapper transactionsMapper) {
        this.transactionService = transactionService;
        this.transactionsMapper = transactionsMapper;
    }

    @PostMapping()
    public ResponseEntity postTransaction(@RequestBody ApiTransaction apiTransaction) {
        transactionService.save(transactionsMapper.map(apiTransaction));
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping()
    public ResponseEntity deleteAllTransactions() {
        transactionService.deleteAll();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}