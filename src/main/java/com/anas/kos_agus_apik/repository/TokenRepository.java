package com.anas.kos_agus_apik.repository;

import com.anas.kos_agus_apik.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, String> {
}
