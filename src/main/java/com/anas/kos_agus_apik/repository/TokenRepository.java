package com.anas.kos_agus_apik.repository;

import com.anas.kos_agus_apik.entity.Token;
import com.anas.kos_agus_apik.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {

    Optional<Token> findByUser(User user);

    List<Token> findAllByUser(User user);

}
