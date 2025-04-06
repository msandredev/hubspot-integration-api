package br.com.msandredev.hubspotintegrationapi.service;

import br.com.msandredev.hubspotintegrationapi.client.HubSpotAuthClient;
import br.com.msandredev.hubspotintegrationapi.config.HubSpotProperties;
import br.com.msandredev.hubspotintegrationapi.dto.AuthorizationUrlResponse;
import br.com.msandredev.hubspotintegrationapi.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubSpotAuthService {

    private final HubSpotAuthClient hubSpotAuthClient;
    private final HubSpotProperties hubSpotProperties;

    public AuthorizationUrlResponse getAuthorizationUrl() {
        String scopesString = String.join(" ", hubSpotProperties.getScopes());

        String url = String.format("%s?client_id=%s&redirect_uri=%s&scope=%s",
                hubSpotProperties.getAuthUrl(), hubSpotProperties.getClientId(),
                URLEncoder.encode(hubSpotProperties.getRedirectUri(), StandardCharsets.UTF_8),
                URLEncoder.encode(scopesString, StandardCharsets.UTF_8));

        log.debug("Gerando URL de autorização com: authUrl={}, clientId={}, redirectUri={}",
                hubSpotProperties.getAuthUrl(), hubSpotProperties.getClientId(), hubSpotProperties.getRedirectUri());
        return new AuthorizationUrlResponse(url);
    }

    public TokenResponse handleCallback(String code) {
        log.info("Trocando code por token: code={}, clientId={}, redirectUri={}", code, hubSpotProperties.getClientId(), hubSpotProperties.getRedirectUri());
        return hubSpotAuthClient.exchangeCodeForToken(
                "authorization_code",
                hubSpotProperties.getClientId(),
                hubSpotProperties.getClientSecret(),
                hubSpotProperties.getRedirectUri(),
                code
        );
    }
}
