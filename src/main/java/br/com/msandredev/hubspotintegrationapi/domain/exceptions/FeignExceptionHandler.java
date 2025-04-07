package br.com.msandredev.hubspotintegrationapi.domain.exceptions;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import br.com.msandredev.hubspotintegrationapi.shared.util.LogMessages;

@Slf4j
public class FeignExceptionHandler {

    public static void handleFeignException(FeignException e) {
        log.error(LogMessages.TOKEN_RENEWAL_FAILURE, e.contentUTF8());
    }
}

