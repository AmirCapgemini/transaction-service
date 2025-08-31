package com.app.capgemini.transaction.repository;

import com.app.capgemini.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    public Transaction findByBookId(String bookId);
    public Transaction findByUserId(String userId);
}
