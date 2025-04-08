package br.com.msandredev.hubspotintegrationapi.application.controller;

import br.com.msandredev.hubspotintegrationapi.application.dto.exceptions.ErrorResponse;
import br.com.msandredev.hubspotintegrationapi.application.dto.request.HubSpotContactRequest;
import br.com.msandredev.hubspotintegrationapi.application.dto.response.HubSpotContactResponse;
import br.com.msandredev.hubspotintegrationapi.application.service.HubSpotCrmContactService;
import br.com.msandredev.hubspotintegrationapi.domain.exceptions.TokenNotAvailableException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/hubspot/contacts")
@Tag(name = "HubSpot CRM Contact Controller", description = "Controller responsável por gerenciar contatos no CRM do HubSpot.")
public class HubSpotCrmContactController {

    @Autowired
    private HubSpotCrmContactService crmService;

    @Operation(
            summary = "Criar novo contato",
            description = "Cria um novo contato no HubSpot CRM com os dados fornecidos. Requer autenticação OAuth válida.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do contato a ser criado",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HubSpotContactRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Contato Básico",
                                            summary = "Exemplo mínimo de criação",
                                            value = """
                        {
                          "email": "novo.contato@exemplo.com",
                          "firstname": "João",
                          "lastname": "Silva"
                        }
                        """
                                    ),
                                    @ExampleObject(
                                            name = "Contato Completo",
                                            summary = "Exemplo com todos os campos",
                                            value = """
                        {
                          "email": "contato.completo@exemplo.com",
                          "firstname": "Maria",
                          "lastname": "Santos",
                          "phone": "+5511999999999",
                          "company": "Empresa Exemplo",
                          "website": "https://exemplo.com",
                          "lifecyclestage": "lead",
                          "custom_properties": {
                            "custom_field": "Valor personalizado"
                          }
                        }
                        """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Contato criado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = HubSpotContactResponse.class),
                                    examples = @ExampleObject(
                                            name = "Resposta de sucesso",
                                            value = """
                        {
                          "id": "12345",
                          "properties": {
                            "email": "novo.contato@exemplo.com",
                            "firstname": "João",
                            "lastname": "Silva",
                            "createdate": "2023-05-20T10:30:00Z"
                          },
                          "createdAt": "2023-05-20T10:30:00Z",
                          "updatedAt": "2023-05-20T10:30:00Z",
                          "archived": false
                        }
                        """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos",
                            content = @Content(
                                    examples = @ExampleObject(
                                            value = """
                        {
                          "code": "INVALID_DATA",
                          "message": "O campo email é obrigatório"
                        }
                        """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Autenticação necessária",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                        {
                          "code": "AUTH_REQUIRED",
                          "message": "Autenticação com HubSpot necessária. Acesse /api/hubspot/auth-url"
                        }
                        """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno no servidor",
                            content = @Content(
                                    examples = @ExampleObject(
                                            value = """
                        {
                          "code": "INTERNAL_ERROR",
                          "message": "Ocorreu um erro ao processar a requisição"
                        }
                        """
                                    )
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<?> createContact(@Valid @RequestBody HubSpotContactRequest request) {
        try {
            return ResponseEntity.ok(crmService.createContact(request));
        } catch (TokenNotAvailableException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(
                            "AUTH_REQUIRED",
                            "Autenticação com HubSpot necessária. Acesse /api/hubspot/auth-url"
                    ));
        }
    }
}
