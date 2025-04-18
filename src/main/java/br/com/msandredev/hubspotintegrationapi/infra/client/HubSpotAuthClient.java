package br.com.msandredev.hubspotintegrationapi.infra.client;

import br.com.msandredev.hubspotintegrationapi.infra.config.HubSpotAuthProperties;
import br.com.msandredev.hubspotintegrationapi.application.dto.response.TokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hubspot-auth-client", url = "${hubspot.token-url}", configuration = HubSpotAuthProperties.class)
public interface HubSpotAuthClient {

    @PostMapping(consumes = "application/x-www-form-urlencoded")
    TokenResponse exchangeCodeForToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam("code") String code
    );

    @PostMapping(consumes = "application/x-www-form-urlencoded")
    TokenResponse refreshToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("refresh_token") String refreshToken,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret
    );
}