package br.com.msandredev.hubspotintegrationapi.service;

import br.com.msandredev.hubspotintegrationapi.dto.TokenResponse;
import br.com.msandredev.hubspotintegrationapi.model.HubSpotToken;
import br.com.msandredev.hubspotintegrationapi.repository.HubSpotTokenRepository;
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

    public void storeTokens(TokenResponse tokenResponse) {
        Optional<String> currentValidToken = getValidAccessToken();

        if (currentValidToken.isPresent()) {
            log.info("Já existe um token válido. Não será salvo um novo.");
            return;
        }

        HubSpotToken token = new HubSpotToken();
        token.setAccessToken(tokenResponse.accessToken());
        token.setRefreshToken(tokenResponse.refreshToken());
        token.setExpiresAt(Instant.now().plusSeconds(tokenResponse.expiresIn()));

        tokenRepository.save(token);
        log.info("Token salvo no banco. Expira em: {}", token.getExpiresAt());
    }

    public Optional<String> getValidAccessToken() {
        Optional<HubSpotToken> latestToken = tokenRepository.findTopByOrderByCreatedAtDesc();

        if (latestToken.isEmpty()) {
            log.warn("Nenhum token encontrado no banco.");
            return Optional.empty();
        }

        HubSpotToken token = latestToken.get();

        if (Instant.now().isAfter(token.getExpiresAt())) {
            log.warn("Token expirado em {}", token.getExpiresAt());
            return Optional.empty();
        }

        log.debug("Token válido encontrado (Expira em: {})", token.getExpiresAt());
        return Optional.of("Bearer " + token.getAccessToken());
    }

    public Optional<String> getRefreshToken() {
        return tokenRepository.findTopByOrderByCreatedAtDesc()
                .map(HubSpotToken::getRefreshToken);
    }

    public Optional<HubSpotToken> findLatestToken() {
        return tokenRepository.findTopByOrderByCreatedAtDesc();
    }
}