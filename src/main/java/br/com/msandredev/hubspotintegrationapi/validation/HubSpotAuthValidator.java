package br.com.msandredev.hubspotintegrationapi.validation;

import br.com.msandredev.hubspotintegrationapi.config.HubSpotAuthProperties;
import br.com.msandredev.hubspotintegrationapi.dto.auth.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubSpotAuthValidator {

    private final HubSpotAuthProperties hubSpotAuthProperties;
    private final TokenValidator tokenValidator;

    public String generateAuthorizationUrl() {
        String scopesString = String.join(" ", hubSpotAuthProperties.getScopes());

        return String.format("%s?client_id=%s&redirect_uri=%s&scope=%s",
                hubSpotAuthProperties.getAuthUrl(), hubSpotAuthProperties.getClientId(),
                URLEncoder.encode(hubSpotAuthProperties.getRedirectUri(), StandardCharsets.UTF_8),
                URLEncoder.encode(scopesString, StandardCharsets.UTF_8));
    }

    public Optional<TokenResponse> getExistingToken() {
        return tokenValidator.getValidToken().map(token ->
                new TokenResponse(token.getAccessToken(), token.getRefreshToken(),
                        (int) (token.getExpiresAt().getEpochSecond() - Instant.now().getEpochSecond())));
    }
    public void handleUnexpectedException(Exception e) {
        log.error("Erro inesperado: {}", e.getMessage());
    }
}
