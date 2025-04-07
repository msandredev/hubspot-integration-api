package br.com.msandredev.hubspotintegrationapi.util;

public abstract class LogMessages {
    public static final String NO_REFRESH_TOKEN = "Nenhum refresh_token disponível. Requer nova autenticação via OAuth.";
    public static final String TOKEN_RENEWAL_FAILURE = "Falha ao renovar token: {}";
}
