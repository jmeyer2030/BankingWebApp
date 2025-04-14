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
    Optional<Account> findByUser_Id(Long userId);

    Optional<Account> findById(Long Id);

    Optional<Account> findByUser_Username(String username);

    boolean existsByUser_Username(String username);

    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance + :delta WHERE a.id = :accountId")
    void adjustBalance(@Param("accountId") Long accountId, @Param("delta") Long delta);
}