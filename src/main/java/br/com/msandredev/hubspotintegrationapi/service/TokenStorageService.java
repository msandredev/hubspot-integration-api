package br.com.msandredev.hubspotintegrationapi.service;

import br.com.msandredev.hubspotintegrationapi.dto.TokenResponse;
import br.com.msandredev.hubspotintegrationapi.model.HubSpotToken;
import br.com.msandredev.hubspotintegrationapi.repository.HubSpotTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenStorageService {

    private final HubSpotTokenRepository tokenRepository;

    @Transactional
    public void storeTokens(TokenResponse tokenResponse) {
        log.info("Recebendo TokenResponse para armazenar: {}", tokenResponse);

        HubSpotToken token = new HubSpotToken();
        token.setAccessToken(tokenResponse.accessToken().replace("\"", ""));
        token.setRefreshToken(tokenResponse.refreshToken().replace("\"", ""));
        token.setExpiresAt(Instant.now().plusSeconds(tokenResponse.expiresIn()));
        token.setCreatedAt(Instant.now());
        token.setUpdatedAt(Instant.now());
        log.info("Criando novo HubSpotToken: {}", token);

        HubSpotToken savedToken = tokenRepository.save(token);
        log.info("Token salvo com ID: {}", savedToken.getId());
    }

    public Optional<String> getRefreshToken() {
        return tokenRepository.findTopByOrderByCreatedAtDesc()
                .map(HubSpotToken::getRefreshToken);
    }
}