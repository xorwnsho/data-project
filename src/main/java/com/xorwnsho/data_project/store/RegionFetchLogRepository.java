package com.xorwnsho.data_project.store;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RegionFetchLogRepository extends JpaRepository<RegionFetchLog, Long> {

	Optional<RegionFetchLog> findFirstByRegionKeyAndFetchedAtAfter(String regionKey, LocalDateTime after);
}
