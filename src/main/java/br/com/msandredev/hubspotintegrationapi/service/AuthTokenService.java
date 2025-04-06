package br.com.msandredev.hubspotintegrationapi.service;

import br.com.msandredev.hubspotintegrationapi.client.HubSpotAuthClient;
import br.com.msandredev.hubspotintegrationapi.config.HubSpotProperties;
import br.com.msandredev.hubspotintegrationapi.dto.TokenResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private final HubSpotAuthClient authClient;
    private final TokenStorageService tokenStorage;
    private final HubSpotProperties hubSpotProperties;

    @Transactional
    public void refreshAccessToken() {
        Optional<String> validToken = tokenStorage.getValidAccessToken();

        if (validToken.isPresent()) {
            log.info("Token válido encontrado no banco. Não é necessário gerar um novo.");
            return;
        }

        Optional<String> refreshToken = tokenStorage.getRefreshToken();

        if (refreshToken.isEmpty()) {
            log.warn("Nenhum refresh_token encontrado. É necessário autenticar novamente via OAuth.");
            return;
        }

        log.info("Gerando novo token via refresh_token...");
        TokenResponse newToken = authClient.refreshToken(
                "refresh_token",
                refreshToken.get(),
                hubSpotProperties.getClientId(),
                hubSpotProperties.getClientSecret()
        );

        tokenStorage.storeTokens(newToken);
        log.info("Novo token gerado e salvo no banco. Expira em: {}",
                Instant.now().plusSeconds(newToken.expiresIn()));
    }
}