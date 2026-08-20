package com.xorwnsho.data_project.geo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * OpenStreetMap Nominatim 지오코딩 (무료, 키 불필요).
 * 사용 정책상 초당 1회 이하로 호출해야 하므로 GeocodingService에서 호출 간격을 둔다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NominatimClient {

	private final RestClient nominatimRestClient;

	public List<NominatimResultDto> search(String query) {
		try {
			NominatimResultDto[] results = nominatimRestClient.get()
					.uri(uriBuilder -> uriBuilder
							.path("/search")
							.queryParam("q", query)
							.queryParam("format", "json")
							.queryParam("limit", 1)
							.queryParam("countrycodes", "kr")
							.build())
					.retrieve()
					.body(NominatimResultDto[].class);
			return results == null ? List.of() : List.of(results);
		} catch (Exception e) {
			log.warn("Nominatim 지오코딩 실패 (query={}): {}", query, e.getMessage());
			return List.of();
		}
	}
}
