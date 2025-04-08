package br.com.msandredev.hubspotintegrationapi.application.service;

import br.com.msandredev.hubspotintegrationapi.shared.validation.HubSpotSignatureValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubSpotWebhookService {

    private final HubSpotSignatureValidator signatureValidator;

    public ResponseEntity<String> validateSignature(String signature, String timestamp, String rawBody, HttpServletRequest request) {
        return signatureValidator.validateSignature(signature, timestamp, rawBody, request);
    }
}
