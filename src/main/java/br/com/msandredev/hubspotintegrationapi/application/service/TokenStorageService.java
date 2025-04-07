package br.com.msandredev.hubspotintegrationapi.application.service;

import br.com.msandredev.hubspotintegrationapi.domain.entities.HubSpotToken;
import br.com.msandredev.hubspotintegrationapi.application.dto.response.TokenResponse;
import br.com.msandredev.hubspotintegrationapi.domain.exceptions.HubSpotExceptionHandler;
import br.com.msandredev.hubspotintegrationapi.domain.repository.HubSpotTokenRepository;
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

    public Optional<String> getValidAccessToken() {
        Optional<HubSpotToken> latestToken = tokenRepository.findTopByOrderByCreatedAtDesc();

        if (latestToken.isEmpty()) {
            log.warn("Nenhum token encontrado no banco");
            return Optional.empty();
        }
        HubSpotToken token = latestToken.get();
        return Optional.of("Bearer " + token.getAccessToken());
    }

    @Transactional
    public void storeTokens(TokenResponse tokenResponse) {
        try {
            clearExistingTokens();
            saveNewToken(tokenResponse);
        } catch (Exception e) {
            log.error("Erro ao armazenar o token: {}", e.getMessage());
            HubSpotExceptionHandler.handleInternalException(e);
        }
    }

    private void clearExistingTokens() {
        tokenRepository.deleteAllInBatch();
    }

    private void saveNewToken(TokenResponse tokenResponse) {
        HubSpotToken token = new HubSpotToken();
        token.setAccessToken(tokenResponse.accessToken());
        token.setRefreshToken(tokenResponse.refreshToken());
        token.setExpiresAt(Instant.now().plusSeconds(tokenResponse.expiresIn()));
        tokenRepository.save(token);
        log.info("Token salvo com ID: {}", token.getId());
    }

    public Optional<HubSpotToken> findLatestToken() {
        return tokenRepository.findTopByOrderByCreatedAtDesc();
    }
}
