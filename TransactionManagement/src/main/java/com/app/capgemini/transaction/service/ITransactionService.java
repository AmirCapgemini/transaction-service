package com.app.capgemini.transaction.service;

import com.app.capgemini.transaction.request.TransactionRequest;
import com.app.capgemini.transaction.request.TransactionUpdateRequest;
import org.springframework.http.ResponseEntity;

public interface ITransactionService {

    public ResponseEntity<?> createTransaction(TransactionRequest transactionRequest);
    public ResponseEntity<?> updateTransaction(TransactionUpdateRequest transactionUpdateRequest);
    public ResponseEntity<?> getTransactionBookId(String bookId);
    public ResponseEntity<?> getTransactionUserId(String userId);
    public ResponseEntity<?> deleteTransaction(Long id);

    public ResponseEntity<?> getTransactionAll();

}
