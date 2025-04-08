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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name = "HubSpot Authentication Controller", description = "Controller responsável por gerenciar o fluxo de autenticação com o HubSpot.")
public class HubSpotAuthController {

    @Autowired
    private HubSpotAuthService hubSpotAuthService;

    @Autowired
    private HubSpotAuthValidator hubSpotAuthValidator;

    @Operation(
        summary = "Obter URL de autorização",
        description = "Retorna a URL de autorização para iniciar o processo de autenticação com o HubSpot.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "URL de autorização gerada com sucesso.",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                        value = "{\"authorizationUrl\": \"https://app.hubspot.com/oauth/authorize?client_id=CLIENT_ID&redirect_uri=REDIRECT_URI&scope=SCOPES\"}"
                    )
                )
            )
        }
    )
    @GetMapping("/authorize")
    public ResponseEntity<AuthorizationUrlResponse> getAuthUrl() {
        return ResponseEntity.ok(hubSpotAuthService.getAuthorizationUrl());
    }

    @Operation(
        summary = "Callback de autenticação",
        description = "Processa o código de autorização retornado pelo HubSpot e obtém o token de acesso.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Token de acesso gerado com sucesso.",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                        value = "{\"accessToken\": \"ACCESS_TOKEN\", \"refreshToken\": \"REFRESH_TOKEN\", \"expiresIn\": 3600}"
                    )
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Erro ao processar o código de autorização.",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                        value = "{\"error\": \"invalid_grant\", \"error_description\": \"Authorization code expired.\"}"
                    )
                )
            )
        }
    )
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
