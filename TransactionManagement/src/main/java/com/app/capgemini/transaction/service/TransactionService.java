package com.app.capgemini.transaction.service;

import com.app.capgemini.transaction.repository.TransactionRepository;
import com.app.capgemini.transaction.request.TransactionRequest;
import com.app.capgemini.transaction.request.TransactionUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TransactionService implements ITransactionService{

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public ResponseEntity<?> createTransaction(TransactionRequest transactionRequest) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateTransaction(TransactionUpdateRequest transactionUpdateRequest) {
        return null;
    }

    @Override
    public ResponseEntity<?> getTransactionBookId(String bookId) {
        return null;
    }

    @Override
    public ResponseEntity<?> getTransactionUserId(String userId) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteTransaction(Long id) {
        return null;
    }
}
