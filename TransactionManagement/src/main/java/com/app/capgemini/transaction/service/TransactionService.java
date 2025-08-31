package com.app.capgemini.transaction.service;

import com.app.capgemini.transaction.model.Transaction;
import com.app.capgemini.transaction.repository.TransactionRepository;
import com.app.capgemini.transaction.request.TransactionRequest;
import com.app.capgemini.transaction.request.TransactionUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService implements ITransactionService{

    @Autowired
    private TransactionRepository transactionRepository;


    @Override
    public ResponseEntity<?> createTransaction(TransactionRequest transactionRequest) {
        try {
            Transaction transaction = new Transaction();
            transaction.setBookId(transactionRequest.getBookId());
            transaction.setUserId(transactionRequest.getUserId());
            transaction.setIssueDate(transactionRequest.getIssueDate());
            transaction.setReturnDate(transactionRequest.getReturnDate());
            transaction.setStatus("Issue");
            transaction.setFine(transactionRequest.getFine());

            Transaction savedTransaction = transactionRepository.save(transaction);
            return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>("Failed to create Transaction: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> updateTransaction(TransactionUpdateRequest transactionUpdateRequest) {
        try {
            if (transactionUpdateRequest.getId() != null) {
                Optional<Transaction> transactionById = transactionRepository.findById(transactionUpdateRequest.getId());
                if (transactionById.isPresent()) {
                    Transaction transaction = getTransaction(transactionUpdateRequest, transactionById);
                    Transaction updatedTransaction = transactionRepository.save(transaction);
                    return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Transaction not found", HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>("Transaction ID is required", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>("Failed to update Transaction: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static final int MAX_BORROW_DAYS = 14;
    private static final double FINE_PER_DAY = 5.0;

    private static Transaction getTransaction(TransactionUpdateRequest transactionUpdateRequest, Optional<Transaction> transactionById) {
        Transaction transaction = transactionById.orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setBookId(transactionUpdateRequest.getBookId());
        transaction.setUserId(transactionUpdateRequest.getUserId());
        transaction.setIssueDate(transactionUpdateRequest.getIssueDate());
        transaction.setReturnDate(transactionUpdateRequest.getReturnDate());

        // Fine calculation logic
        long daysBetween = ChronoUnit.DAYS.between(transaction.getIssueDate(), transaction.getReturnDate());
        if (daysBetween > MAX_BORROW_DAYS) {
            double fine = (daysBetween - MAX_BORROW_DAYS) * FINE_PER_DAY;
            transaction.setFine(fine);
        } else {
            transaction.setFine(0.0);
        }

        transaction.setStatus("RETURN");
        return transaction;
    }


    @Override
    public ResponseEntity<?> getTransactionBookId(String bookId) {
        try {
            Transaction transaction = transactionRepository.findByBookId(bookId);
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>("Failed to transaction: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @Override
    public ResponseEntity<?> getTransactionUserId(String userId) {
        try {
            Transaction transaction = transactionRepository.findByUserId(userId);
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>("Failed to transaction: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


    @Override
    public ResponseEntity<?> deleteTransaction(Long id) {
        try {
            transactionRepository.deleteById(id);
            return ResponseEntity.ok("Transaction deleted successfully");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>("Failed to delete Transaction: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<?> getTransactionAll() {
        try {
            List<Transaction> transaction = transactionRepository.findAll();
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>("Failed to transaction: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
