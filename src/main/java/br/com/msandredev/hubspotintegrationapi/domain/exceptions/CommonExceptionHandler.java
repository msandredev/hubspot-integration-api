package br.com.msandredev.hubspotintegrationapi.domain.exceptions;

import br.com.msandredev.hubspotintegrationapi.application.dto.exceptions.HubSpotErrorResponse;
import feign.FeignException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import br.com.msandredev.hubspotintegrationapi.application.dto.exceptions.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        "INTERNAL_SERVER_ERROR",
                        "Ocorreu um erro interno"));
    }

    @ExceptionHandler(RequestNotPermitted.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ErrorResponse handleRateLimitExceeded(RequestNotPermitted ex) {
        return new ErrorResponse(
                "RATELIMIT_EXCEEDED",
                "Limite de requisições excedido. Tente novamente em 1 minuto."
        );
    }

    @ExceptionHandler(TokenNotAvailableException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleTokenNotAvailable(TokenNotAvailableException ex) {
        return new ErrorResponse(
                "TOKEN_NOT_AVAILABLE",
                ex.getMessage()
        );
    }

    @ExceptionHandler(HubSpotApiException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponse handleHubSpotApiException(HubSpotApiException ex) {
        return new ErrorResponse(
                "HUBSPOT_AUTH_ERROR",
                ex.getMessage()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRuntimeException(RuntimeException ex) {
        log.error("Erro interno: {}", ex.getMessage());
        return new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "Detalhes do erro foram logados"
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Erro de validação");

        return ResponseEntity.badRequest()
                .body(new ErrorResponse("VALIDATION_ERROR", errorMsg));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("VALIDATION_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(FeignException.Conflict.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public HubSpotErrorResponse handleHubSpotConflict(FeignException e) {
        HubSpotErrorResponse error = HubSpotErrorResponse.fromFeignException(e);
        log.warn("Conflito no HubSpot: {}", error.message());
        return error;
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<HubSpotErrorResponse> handleHubSpotApiException(FeignException e) {
        HubSpotErrorResponse error = HubSpotErrorResponse.fromFeignException(e);
        HttpStatus status = HttpStatus.resolve(e.status());

        log.error("Erro na API HubSpot ({}): {}", status, error.message());
        return ResponseEntity
                .status(status != null ? status : HttpStatus.BAD_GATEWAY)
                .body(error);
    }
}