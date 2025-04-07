package br.com.msandredev.hubspotintegrationapi.service;

import br.com.msandredev.hubspotintegrationapi.domain.entities.HubSpotToken;
import br.com.msandredev.hubspotintegrationapi.dto.auth.TokenResponse;
import br.com.msandredev.hubspotintegrationapi.repository.HubSpotTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

        if (Instant.now().isAfter(token.getExpiresAt().minus(5, ChronoUnit.MINUTES))) {
            log.warn("Token expirado ou próximo de expirar em {}", token.getExpiresAt());
            return Optional.empty();
        }

        return Optional.of("Bearer " + token.getAccessToken());
    }

    @Transactional
    public void storeTokens(TokenResponse tokenResponse) {
        try {
            tokenRepository.deleteAllInBatch();

            HubSpotToken token = new HubSpotToken();
            token.setAccessToken(tokenResponse.accessToken());
            token.setRefreshToken(tokenResponse.refreshToken());
            token.setExpiresAt(Instant.now().plusSeconds(tokenResponse.expiresIn()));

            tokenRepository.save(token);
            log.info("Token salvo com ID: {}", token.getId());

        } catch (Exception e) {
            log.error("Erro ao salvar token: {}", e.getMessage());
            throw new RuntimeException("Falha ao armazenar token no banco");
        }
    }

    public Optional<String> getRefreshToken() {
        return tokenRepository.findTopByOrderByCreatedAtDesc()
                .map(HubSpotToken::getRefreshToken);
    }

    public Optional<HubSpotToken> findLatestToken() {
        return tokenRepository.findTopByOrderByCreatedAtDesc();
    }
}
