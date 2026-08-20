package com.xorwnsho.data_project.store;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/**
 * 반경 내 상가업소 조회. 같은 좌표/반경 조합은 CACHE_TTL_DAYS 이내 재조회 시
 * 외부 API 대신 DB 캐시를 사용한다 (data.go.kr 개발계정 일일 호출 한도 대응).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

	private static final long CACHE_TTL_DAYS = 7;
	private static final double EARTH_RADIUS_METERS = 6_371_000;

	private final StoreApiClient storeApiClient;
	private final StoreRepository storeRepository;
	private final RegionFetchLogRepository regionFetchLogRepository;

	@Transactional
	public List<StoreEntity> findNearby(double lon, double lat, int radiusMeters) {
		String regionKey = regionKey(lon, lat, radiusMeters);
		LocalDateTime cutoff = LocalDateTime.now().minusDays(CACHE_TTL_DAYS);

		boolean cached = regionFetchLogRepository
				.findFirstByRegionKeyAndFetchedAtAfter(regionKey, cutoff)
				.isPresent();

		if (!cached) {
			refreshFromApi(lon, lat, radiusMeters, regionKey);
		}

		return findInDbWithinRadius(lon, lat, radiusMeters);
	}

	private void refreshFromApi(double lon, double lat, int radiusMeters, String regionKey) {
		List<StoreItemDto> items = storeApiClient.findAllInRadius(lon, lat, radiusMeters);
		log.info("상가업소 API 조회: regionKey={}, 건수={}", regionKey, items.size());

		items.stream()
				.filter(item -> item.bizesId() != null && item.lon() != null && item.lat() != null)
				.map(StoreEntity::from)
				.forEach(storeRepository::save);

		regionFetchLogRepository.save(new RegionFetchLog(regionKey));
	}

	private List<StoreEntity> findInDbWithinRadius(double lon, double lat, int radiusMeters) {
		double latDelta = radiusMeters / 111_000.0;
		double lonDelta = radiusMeters / (111_000.0 * Math.cos(Math.toRadians(lat)));

		return storeRepository
				.findByLonBetweenAndLatBetween(lon - lonDelta, lon + lonDelta, lat - latDelta, lat + latDelta)
				.stream()
				.filter(store -> distanceMeters(lon, lat, store.getLon(), store.getLat()) <= radiusMeters)
				.toList();
	}

	private double distanceMeters(double lon1, double lat1, double lon2, double lat2) {
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
				* Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return EARTH_RADIUS_METERS * c;
	}

	private String regionKey(double lon, double lat, int radiusMeters) {
		return String.format(Locale.ROOT, "%.3f_%.3f_%d", lon, lat, radiusMeters);
	}
}
