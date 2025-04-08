package br.com.msandredev.hubspotintegrationapi.application.controller;

import br.com.msandredev.hubspotintegrationapi.application.service.HubSpotWebhookService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hubspot")
public class HubSpotWebhookController {

    @Autowired
    private HubSpotWebhookService service;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestHeader("X-HubSpot-Signature-v3") String signature,
            @RequestHeader("X-HubSpot-Request-Timestamp") String timestamp,
            @RequestBody String rawBody,
            HttpServletRequest request) {

        return service.validateSignature(signature, timestamp, rawBody, request);
    }
}
