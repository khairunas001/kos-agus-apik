package com.anas.kos_agus_apik.repository;

import com.anas.kos_agus_apik.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
