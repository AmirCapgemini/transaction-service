package com.app.capgemini.transaction.service;

import com.app.capgemini.transaction.model.Transaction;
import com.app.capgemini.transaction.repository.TransactionRepository;
import com.app.capgemini.transaction.request.TransactionRequest;
import com.app.capgemini.transaction.request.TransactionUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void testCreateTransaction_Success() {
        TransactionRequest request = new TransactionRequest();
        request.setBookId("B101");
        request.setUserId("U202");
        request.setIssueDate(LocalDate.of(2025, 8, 1));
        request.setReturnDate(LocalDate.of(2025, 8, 10));
        request.setFine(0.0);

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setBookId(request.getBookId());
        transaction.setUserId(request.getUserId());
        transaction.setIssueDate(request.getIssueDate());
        transaction.setReturnDate(request.getReturnDate());
        transaction.setStatus("Issue");
        transaction.setFine(request.getFine());

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        ResponseEntity<?> response = transactionService.createTransaction(request);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Transaction);
        assertEquals("B101", ((Transaction) response.getBody()).getBookId());
    }

    @Test
    void testUpdateTransaction_WithFineCalculation() {
        TransactionUpdateRequest updateRequest = new TransactionUpdateRequest();
        updateRequest.setId(1L);
        updateRequest.setBookId("B101");
        updateRequest.setUserId("U202");
        updateRequest.setIssueDate(LocalDate.of(2025, 8, 1));
        updateRequest.setReturnDate(LocalDate.of(2025, 8, 20)); // 19 days → fine expected

        Transaction existingTransaction = new Transaction();
        existingTransaction.setId(1L);
        existingTransaction.setBookId("B101");
        existingTransaction.setUserId("U202");
        existingTransaction.setIssueDate(LocalDate.of(2025, 8, 1));
        existingTransaction.setReturnDate(LocalDate.of(2025, 8, 10));
        existingTransaction.setStatus("Issue");
        existingTransaction.setFine(0.0);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(existingTransaction));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> response = transactionService.updateTransaction(updateRequest);

        assertEquals(200, response.getStatusCodeValue());
        Transaction updated = (Transaction) response.getBody();
        assertEquals("RETURN", updated.getStatus());
        assertEquals(25.0, updated.getFine()); // (19 - 14) * 5
    }

    @Test
    void testGetTransactionBookId() {
        Transaction transaction = new Transaction();
        transaction.setBookId("B101");

        when(transactionRepository.findByBookId("B101")).thenReturn(transaction);

        ResponseEntity<?> response = transactionService.getTransactionBookId("B101");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("B101", ((Transaction) response.getBody()).getBookId());
    }


    @Test
    void testDeleteTransaction() {
        doNothing().when(transactionRepository).deleteById(1L);

        ResponseEntity<?> response = transactionService.deleteTransaction(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transaction deleted successfully", response.getBody());
    }


    @Test
    void testGetTransactionAll() {
        List<Transaction> transactions = List.of(new Transaction(), new Transaction());
        when(transactionRepository.findAll()).thenReturn(transactions);

        ResponseEntity<?> response = transactionService.getTransactionAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, ((List<?>) response.getBody()).size());
    }
}

