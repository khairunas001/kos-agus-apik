package com.anas.kos_agus_apik.service;

import com.anas.kos_agus_apik.entity.Token;
import com.anas.kos_agus_apik.entity.User;
import com.anas.kos_agus_apik.model.request.LoginUserRequest;
import com.anas.kos_agus_apik.model.response.TokenResponse;
import com.anas.kos_agus_apik.repository.TokenRepository;
import com.anas.kos_agus_apik.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private ValidationService validationService;


    @Transactional
    public TokenResponse login(LoginUserRequest request) {
        validationService.validate(request);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Username or password is wrong"
                ));

        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Username or password is wrong"
            );
        }

        String newToken = UUID.randomUUID().toString();
        LocalDateTime expiredAt = next30Days();

        // hapus token lama kalau user sudah punya
        tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);

        // buat token baru dengan id unik
        Token token = new Token();
        token.setId(UUID.randomUUID().toString()); // jangan pakai user.getId()
        token.setUser(user);
        token.setToken(newToken);
        token.setTokenExpiredAt(expiredAt);

        tokenRepository.save(token);

        return TokenResponse.builder()
                .userId(user.getId())
                .token(newToken)
                .tokenExpiredAt(expiredAt)
                .build();
    }


    // generate 30 days from now
    private LocalDateTime next30Days() {
        return LocalDateTime.now().plusDays(30);
    }
}



