package br.com.msandredev.hubspotintegrationapi.controller;

import br.com.msandredev.hubspotintegrationapi.dto.AuthorizationUrlResponse;
import br.com.msandredev.hubspotintegrationapi.dto.TokenResponse;
import br.com.msandredev.hubspotintegrationapi.service.HubSpotAuthService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<TokenResponse> handleCallback(@RequestParam String code) {
        return ResponseEntity.ok(hubSpotAuthService.handleCallback(code));
    }
}
