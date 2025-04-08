package br.com.msandredev.hubspotintegrationapi.application.controller;

import br.com.msandredev.hubspotintegrationapi.application.dto.response.AuthorizationUrlResponse;
import br.com.msandredev.hubspotintegrationapi.application.dto.response.TokenResponse;
import br.com.msandredev.hubspotintegrationapi.application.service.HubSpotAuthService;
import br.com.msandredev.hubspotintegrationapi.domain.exceptions.HubSpotApiException;
import br.com.msandredev.hubspotintegrationapi.domain.exceptions.HubSpotExceptionHandler;
import br.com.msandredev.hubspotintegrationapi.shared.validation.HubSpotAuthValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/hubspot")
public class HubSpotAuthController {

    @Autowired
    private HubSpotAuthService hubSpotAuthService;

    @Autowired
    private HubSpotAuthValidator hubSpotAuthValidator;

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
