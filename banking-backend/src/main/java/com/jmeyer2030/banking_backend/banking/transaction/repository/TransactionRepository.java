package com.jmeyer2030.banking_backend.banking.transaction.repository;

import com.jmeyer2030.banking_backend.banking.transaction.dto.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t " +
            "WHERE t.senderAccount.id = :accountId OR t.recipientAccount.id = :accountId " +
            "ORDER BY t.timestamp DESC")
    List<Transaction> findTransactionsByUserIdList(@Param("accountId") Long accountId, Pageable pageable);

    // Returns pageable for transaction history
    @Query("SELECT t FROM Transaction t " +
            "WHERE t.senderAccount.id = :accountId OR t.recipientAccount.id = :accountId " +
            "ORDER BY t.timestamp DESC")
    Page<Transaction> findTransactionsByUserId(@Param("accountId") Long accountId, Pageable pageable);
}
