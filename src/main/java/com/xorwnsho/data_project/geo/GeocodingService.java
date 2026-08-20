package com.xorwnsho.data_project.geo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RegionDictionary에 없는 지역명을 위한 폴백 지오코딩.
 * 메시지에서 동/읍/면 단위 지명 후보를 정규식으로 뽑아 대전/세종 소속인지 확인하며 좌표를 조회한다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GeocodingService {

	private static final Pattern DONG_PATTERN = Pattern.compile("[가-힣]{2,8}(동|읍|면)");
	private static final long MIN_CALL_INTERVAL_MS = 1100;

	private final NominatimClient nominatimClient;
	private final GeocodeCacheRepository geocodeCacheRepository;

	private long lastCallAt = 0;

	public Optional<Location> resolve(String message) {
		List<String> cities = message.contains("세종")
				? List.of("세종특별자치시", "대전광역시")
				: List.of("대전광역시", "세종특별자치시");

		for (String candidate : extractCandidates(message)) {
			for (String city : cities) {
				Optional<Location> location = geocode(city, candidate);
				if (location.isPresent()) {
					return location;
				}
			}
		}
		return Optional.empty();
	}

	private Set<String> extractCandidates(String message) {
		Set<String> candidates = new LinkedHashSet<>();
		Matcher matcher = DONG_PATTERN.matcher(message);
		while (matcher.find()) {
			candidates.add(matcher.group());
		}
		return candidates;
	}

	private Optional<Location> geocode(String city, String candidate) {
		String query = city + " " + candidate;

		Optional<GeocodeCache> cached = geocodeCacheRepository.findByQuery(query);
		if (cached.isPresent()) {
			GeocodeCache hit = cached.get();
			return Optional.of(new Location(candidate, hit.getLon(), hit.getLat()));
		}

		throttle();
		List<NominatimResultDto> results = nominatimClient.search(query);
		String cityShort = city.substring(0, 2);

		return results.stream()
				.filter(r -> r.displayName() != null && r.displayName().contains(cityShort))
				.findFirst()
				.flatMap(r -> parse(r, candidate, query));
	}

	private Optional<Location> parse(NominatimResultDto result, String candidate, String query) {
		try {
			double lon = Double.parseDouble(result.lon());
			double lat = Double.parseDouble(result.lat());
			geocodeCacheRepository.save(new GeocodeCache(query, lon, lat));
			log.info("지오코딩 성공: query={} -> ({}, {})", query, lon, lat);
			return Optional.of(new Location(candidate, lon, lat));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}

	private synchronized void throttle() {
		long elapsed = System.currentTimeMillis() - lastCallAt;
		if (elapsed < MIN_CALL_INTERVAL_MS) {
			try {
				Thread.sleep(MIN_CALL_INTERVAL_MS - elapsed);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		lastCallAt = System.currentTimeMillis();
	}
}
