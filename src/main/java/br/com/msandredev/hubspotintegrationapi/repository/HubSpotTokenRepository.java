package br.com.msandredev.hubspotintegrationapi.repository;

import br.com.msandredev.hubspotintegrationapi.model.HubSpotToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HubSpotTokenRepository extends JpaRepository<HubSpotToken, Long> {
    Optional<HubSpotToken> findTopByOrderByCreatedAtDesc();
}
