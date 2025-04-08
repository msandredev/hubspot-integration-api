package br.com.msandredev.hubspotintegrationapi.shared.validation;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class HubSpotSignatureValidator {

    private static final Logger log = LogManager.getLogger(HubSpotSignatureValidator.class);
    @Value("${hubspot.client-secret}")
    private String clientSecret;

    public ResponseEntity<String> validateSignature(String signature, String timestamp, String rawBody, HttpServletRequest request) {
        try {
            String method = request.getMethod();
            String uri = request.getRequestURI() +
                    (request.getQueryString() != null ? "?" + request.getQueryString() : "");

            boolean isValid = isValidSignature(
                    clientSecret,
                    method,
                    uri,
                    rawBody,
                    timestamp,
                    signature
            );

            if (!isValid) {
                log.info("Invalid signature: {}", signature);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public static boolean isValidSignature(
            String clientSecret,
            String method,
            String uri,
            String requestBody,
            String timestamp,
            String providedSignature) throws Exception {

        String sourceString = method + uri + requestBody + timestamp;
        log.info("Source String: {}", sourceString);

        Mac sha256 = Mac.getInstance("HmacSHA256");
        sha256.init(new SecretKeySpec(clientSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] hash = sha256.doFinal(sourceString.getBytes(StandardCharsets.UTF_8));

        String computedSignature = Base64.getEncoder().encodeToString(hash);
        log.info("Computed Signature: {}", computedSignature);

        return computedSignature.equals(providedSignature);
    }
}
