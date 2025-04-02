package com.jmeyer2030.banking_backend.banking.account.repository;


import com.jmeyer2030.banking_backend.banking.account.dto.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUserId(Long userId);

    Optional<Account> findById(Long Id);

    Optional<Account> findByUsername(String username);

    boolean existsByUsername(String username);

    @Modifying
    @Query("UPDATE Account a SET a.balance = :balance WHERE a.id = :accountId")
    void updateBalance(@Param("accountId") Long accountId, @Param("balance") Long balance);
}