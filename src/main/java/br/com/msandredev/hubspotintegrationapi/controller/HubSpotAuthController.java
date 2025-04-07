package br.com.msandredev.hubspotintegrationapi.controller;

import br.com.msandredev.hubspotintegrationapi.dto.auth.AuthorizationUrlResponse;
import br.com.msandredev.hubspotintegrationapi.dto.auth.TokenResponse;
import br.com.msandredev.hubspotintegrationapi.dto.exceptions.ErrorResponse;
import br.com.msandredev.hubspotintegrationapi.exceptions.HubSpotApiException;
import br.com.msandredev.hubspotintegrationapi.service.HubSpotAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hubspot")
@RequiredArgsConstructor
public class HubSpotAuthController {

    private final HubSpotAuthService hubSpotAuthService;

    @GetMapping("/auth-url")
    public ResponseEntity<AuthorizationUrlResponse> getAuthUrl() {
        return ResponseEntity.ok(hubSpotAuthService.getAuthorizationUrl());
    }

    @GetMapping("/callback")
    public ResponseEntity<?> handleCallback(@RequestParam String code) {
        try {
            TokenResponse response = hubSpotAuthService.handleCallback(code);
            return ResponseEntity.ok(response);
        } catch (HubSpotApiException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(new ErrorResponse("HUBSPOT_ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("INTERNAL_ERROR", "Erro no processamento interno."));
        }
    }
}
