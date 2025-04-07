package br.com.msandredev.hubspotintegrationapi.service;

import br.com.msandredev.hubspotintegrationapi.client.HubSpotAuthClient;
import br.com.msandredev.hubspotintegrationapi.config.HubSpotAuthProperties;
import br.com.msandredev.hubspotintegrationapi.dto.auth.AuthorizationUrlResponse;
import br.com.msandredev.hubspotintegrationapi.dto.auth.TokenResponse;
import br.com.msandredev.hubspotintegrationapi.exceptions.FeignExceptionHandler;
import br.com.msandredev.hubspotintegrationapi.exceptions.HubSpotApiException;
import br.com.msandredev.hubspotintegrationapi.validation.HubSpotAuthValidator;
import br.com.msandredev.hubspotintegrationapi.validation.TokenValidator;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubSpotAuthService {

    private final HubSpotAuthClient hubSpotAuthClient;
    private final HubSpotAuthProperties hubSpotAuthProperties;
    private final TokenStorageService tokenStorageService;
    private final HubSpotAuthValidator hubSpotAuthValidator;
    private final TokenValidator tokenValidator;

    public AuthorizationUrlResponse getAuthorizationUrl() {
        if (tokenValidator.isTokenValid()) {
            return new AuthorizationUrlResponse("Token atual ainda é válido. Não é necessário gerar um novo.");
        }

        String url = hubSpotAuthValidator.generateAuthorizationUrl();
        log.debug("Gerando URL de autorização com: authUrl={}, clientId={}, redirectUri={}",
                hubSpotAuthProperties.getAuthUrl(), hubSpotAuthProperties.getClientId(), hubSpotAuthProperties.getRedirectUri());
        return new AuthorizationUrlResponse(url);
    }

    public TokenResponse handleCallback(String code) {
        try {
            tokenValidator.logExistingToken();
            TokenResponse tokenResponse = exchangeCodeForToken(code);
            tokenStorageService.storeTokens(tokenResponse);
            return tokenResponse;
        } catch (FeignException e) {
            FeignExceptionHandler.handleFeignException(e);
            throw new HubSpotApiException("Falha na autenticação com HubSpot");
        } catch (Exception e) {
            hubSpotAuthValidator.handleUnexpectedException(e);
            throw new RuntimeException("Erro interno ao processar callback");
        }
    }

    private TokenResponse exchangeCodeForToken(String code) {
        return hubSpotAuthClient.exchangeCodeForToken(
                "authorization_code",
                hubSpotAuthProperties.getClientId(),
                hubSpotAuthProperties.getClientSecret(),
                hubSpotAuthProperties.getRedirectUri(),
                code
        );
    }
}
