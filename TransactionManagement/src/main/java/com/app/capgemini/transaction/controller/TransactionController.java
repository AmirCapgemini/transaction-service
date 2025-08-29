package com.app.capgemini.transaction.controller;

import com.app.capgemini.transaction.request.TransactionRequest;
import com.app.capgemini.transaction.request.TransactionUpdateRequest;
import com.app.capgemini.transaction.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    @Autowired
    private ITransactionService transactionService;


    @PostMapping("/createTransaction")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionRequest transactionRequest) {
        return transactionService.createTransaction(transactionRequest);
    }

    @PutMapping("/updateTransaction")
    public ResponseEntity<?> updateTransaction(@RequestBody TransactionUpdateRequest transactionRequest) {
        return transactionService.updateTransaction(transactionRequest);
    }


    @GetMapping("/getByTransactionUserId/{userId}")
    public ResponseEntity<?> getBytransaction(@PathVariable("userId") String userId) {
        return transactionService.getTransactionUserId(userId);
    }

    @GetMapping("/getByTransactionBookId/{bookId}")
    public ResponseEntity<?> getTransactionBybookId(@PathVariable("bookId") String bookId) {
        return transactionService.getTransactionBookId(bookId);
    }

    @GetMapping("/deleteTransactionById/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable("id") Long id) {
        return transactionService.deleteTransaction(id);
    }



}
