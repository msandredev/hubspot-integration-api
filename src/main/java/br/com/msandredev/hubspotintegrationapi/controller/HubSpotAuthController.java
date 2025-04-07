package br.com.msandredev.hubspotintegrationapi.controller;

import br.com.msandredev.hubspotintegrationapi.dto.auth.AuthorizationUrlResponse;
import br.com.msandredev.hubspotintegrationapi.dto.auth.TokenResponse;
import br.com.msandredev.hubspotintegrationapi.exceptions.HubSpotApiException;
import br.com.msandredev.hubspotintegrationapi.service.HubSpotAuthService;
import br.com.msandredev.hubspotintegrationapi.exceptions.HubSpotExceptionHandler;
import br.com.msandredev.hubspotintegrationapi.validation.HubSpotAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/hubspot")
@RequiredArgsConstructor
public class HubSpotAuthController {

    private final HubSpotAuthService hubSpotAuthService;
    private final HubSpotAuthValidator hubSpotAuthValidator;

    @GetMapping("/auth-url")
    public ResponseEntity<AuthorizationUrlResponse> getAuthUrl() {
        return ResponseEntity.ok(hubSpotAuthService.getAuthorizationUrl());
    }

    @GetMapping("/callback")
    public ResponseEntity<?> handleCallback(@RequestParam String code) {
        try {
            Optional<TokenResponse> existingToken = hubSpotAuthValidator.getExistingToken();
            if (existingToken.isPresent()) {
                return ResponseEntity.ok(existingToken.get());
            }
            TokenResponse response = hubSpotAuthService.handleCallback(code);
            return ResponseEntity.ok(response);
        } catch (HubSpotApiException e) {
            return HubSpotExceptionHandler.handleHubSpotApiException(e);
        } catch (Exception e) {
            return HubSpotExceptionHandler.handleInternalException(e);
        }
    }
}
