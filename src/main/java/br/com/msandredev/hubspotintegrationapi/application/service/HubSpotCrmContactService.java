package br.com.msandredev.hubspotintegrationapi.application.service;

import br.com.msandredev.hubspotintegrationapi.infra.client.HubSpotCrmClient;
import br.com.msandredev.hubspotintegrationapi.application.dto.request.HubSpotContactRequest;
import br.com.msandredev.hubspotintegrationapi.application.dto.response.HubSpotContactResponse;
import br.com.msandredev.hubspotintegrationapi.domain.exceptions.TokenNotAvailableException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubSpotCrmContactService {

    private final HubSpotCrmClient crmClient;
    private final TokenStorageService tokenStorage;
    private final ObjectMapper objectMapper;

    @RateLimiter(name = "hubspot-rate-limit")
    public HubSpotContactResponse createContact(HubSpotContactRequest request) {
        String accessToken = tokenStorage.getValidAccessToken()
                .orElseThrow(() -> new TokenNotAvailableException("Token inválido ou expirado"));

        try {
            ResponseEntity<String> response = crmClient.createContact(request, accessToken);
            return objectMapper.readValue(response.getBody(), HubSpotContactResponse.class);
        } catch (FeignException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Falha ao processar resposta do HubSpot", e);
        }
    }
}
