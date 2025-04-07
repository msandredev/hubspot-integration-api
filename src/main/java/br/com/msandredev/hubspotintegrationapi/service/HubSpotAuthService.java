package br.com.msandredev.hubspotintegrationapi.service;

import br.com.msandredev.hubspotintegrationapi.client.HubSpotAuthClient;
import br.com.msandredev.hubspotintegrationapi.config.HubSpotAuthProperties;
import br.com.msandredev.hubspotintegrationapi.domain.entities.HubSpotToken;
import br.com.msandredev.hubspotintegrationapi.dto.auth.AuthorizationUrlResponse;
import br.com.msandredev.hubspotintegrationapi.dto.auth.TokenResponse;
import br.com.msandredev.hubspotintegrationapi.exceptions.HubSpotApiException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubSpotAuthService {

    private final HubSpotAuthClient hubSpotAuthClient;
    private final HubSpotAuthProperties hubSpotAuthProperties;
    private final TokenStorageService tokenStorageService;

    public AuthorizationUrlResponse getAuthorizationUrl() {
        String scopesString = String.join(" ", hubSpotAuthProperties.getScopes());

        String url = String.format("%s?client_id=%s&redirect_uri=%s&scope=%s",
                hubSpotAuthProperties.getAuthUrl(), hubSpotAuthProperties.getClientId(),
                URLEncoder.encode(hubSpotAuthProperties.getRedirectUri(), StandardCharsets.UTF_8),
                URLEncoder.encode(scopesString, StandardCharsets.UTF_8));

        log.debug("Gerando URL de autorização com: authUrl={}, clientId={}, redirectUri={}",
                hubSpotAuthProperties.getAuthUrl(), hubSpotAuthProperties.getClientId(), hubSpotAuthProperties.getRedirectUri());
        return new AuthorizationUrlResponse(url);
    }

    public TokenResponse handleCallback(String code) {
        try {
            Optional<HubSpotToken> existingToken = tokenStorageService.findLatestToken();
            existingToken.ifPresent(token ->
                    log.info("Token existente expira em: {}", token.getExpiresAt())
            );

            TokenResponse tokenResponse = hubSpotAuthClient.exchangeCodeForToken(
                    "authorization_code",
                    hubSpotAuthProperties.getClientId(),
                    hubSpotAuthProperties.getClientSecret(),
                    hubSpotAuthProperties.getRedirectUri(),
                    code
            );

            tokenStorageService.storeTokens(tokenResponse);
            return tokenResponse;

        } catch (FeignException e) {
            log.error("Erro ao trocar code por token: {}", e.contentUTF8());
            throw new HubSpotApiException("Falha na autenticação com HubSpot");
        } catch (Exception e) {
            log.error("Erro inesperado: {}", e.getMessage());
            throw new RuntimeException("Erro interno ao processar callback");
        }
    }
}
