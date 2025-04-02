package com.jmeyer2030.banking_backend.banking.transaction.repository;

import com.jmeyer2030.banking_backend.banking.transaction.dto.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    @Query("SELECT t FROM Transaction t " +
            "WHERE t.senderId = :userId OR t.recipientId = :userId " +
            "ORDER BY t.timestamp DESC")
    List<Transaction> findTop10RecentTransactionsByUserId(@Param("userId") Long userId, Pageable pageable);
}
