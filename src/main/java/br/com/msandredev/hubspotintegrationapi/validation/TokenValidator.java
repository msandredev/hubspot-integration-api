package br.com.msandredev.hubspotintegrationapi.validation;

import br.com.msandredev.hubspotintegrationapi.domain.entities.HubSpotToken;
import br.com.msandredev.hubspotintegrationapi.service.TokenStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenValidator {

    private final TokenStorageService tokenStorageService;

    public boolean isTokenExpired(HubSpotToken token) {
        return Instant.now().isAfter(token.getExpiresAt());
    }

    public Optional<HubSpotToken> getValidToken() {
        Optional<HubSpotToken> latestToken = tokenStorageService.findLatestToken();
        if (latestToken.isPresent() && isTokenAboutToExpire(latestToken.get())) {
            return Optional.empty();
        }
        return latestToken.filter(token -> !isTokenExpired(token));
    }

    private boolean isTokenAboutToExpire(HubSpotToken token) {
        // TODO: Para fins de teste, vou deixar 29 minutos pra expirar em 1 minuto (30 minutos)
        //  mas pode ser alterado para 5 minutos por exemplo antes de expirar
        return Instant.now().isAfter(token.getExpiresAt().minus(29, ChronoUnit.MINUTES));
    }

    public boolean isTokenValid() {
        Optional<HubSpotToken> latestToken = getValidToken();
        return latestToken.isPresent();
    }

    public void logExistingToken() {
        Optional<HubSpotToken> existingToken = tokenStorageService.findLatestToken();
        existingToken.ifPresent(token ->
                log.info("Token existente expira em: {}", token.getExpiresAt())
        );
    }
}