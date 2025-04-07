package br.com.msandredev.hubspotintegrationapi.service;

import br.com.msandredev.hubspotintegrationapi.client.HubSpotAuthClient;
import br.com.msandredev.hubspotintegrationapi.config.HubSpotAuthProperties;
import br.com.msandredev.hubspotintegrationapi.dto.auth.TokenResponse;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private final HubSpotAuthClient authClient;
    private final TokenStorageService tokenStorage;
    private final HubSpotAuthProperties hubSpotAuthProperties;

    @Transactional
    public void refreshAccessToken() {
        Optional<String> refreshToken = tokenStorage.getRefreshToken();

        if (refreshToken.isEmpty()) {
            log.warn("Nenhum refresh_token disponível. Requer nova autenticação via OAuth.");
            return;
        }

        try {
            TokenResponse newToken = authClient.refreshToken(
                    "refresh_token",
                    refreshToken.get(),
                    hubSpotAuthProperties.getClientId(),
                    hubSpotAuthProperties.getClientSecret()
            );

            tokenStorage.storeTokens(newToken);

        } catch (FeignException e) {
            log.error("Falha ao renovar token: {}", e.contentUTF8());
        }
    }
}