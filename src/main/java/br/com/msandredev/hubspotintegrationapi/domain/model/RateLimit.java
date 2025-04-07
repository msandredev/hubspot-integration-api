package br.com.msandredev.hubspotintegrationapi.domain.model;

import lombok.Data;

@Data
public class RateLimit {
    private int maxRequests;
    private int interval;
}
