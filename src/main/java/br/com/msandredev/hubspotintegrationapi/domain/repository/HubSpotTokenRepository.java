package br.com.msandredev.hubspotintegrationapi.domain.repository;

import br.com.msandredev.hubspotintegrationapi.domain.entities.HubSpotToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HubSpotTokenRepository extends JpaRepository<HubSpotToken, Long> {
    Optional<HubSpotToken> findTopByOrderByCreatedAtDesc();
}
