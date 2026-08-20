package com.xorwnsho.data_project.geo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GeocodeCacheRepository extends JpaRepository<GeocodeCache, Long> {

	Optional<GeocodeCache> findByQuery(String query);
}
