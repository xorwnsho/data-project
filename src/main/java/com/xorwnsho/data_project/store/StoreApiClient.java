package com.xorwnsho.data_project.store;

import com.xorwnsho.data_project.config.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

/**
 * 소상공인시장진흥공단 상가업소정보 반경조회 API 클라이언트.
 * 개발계정 일일 호출 한도가 있어 StoreService에서 캐싱 후 호출한다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class StoreApiClient {

	private static final int PAGE_SIZE = 1000;
	private static final int MAX_PAGES = 10;

	private final RestClient dataGoKrRestClient;
	private final AppProperties appProperties;

	public List<StoreItemDto> findAllInRadius(double lon, double lat, int radiusMeters) {
		List<StoreItemDto> result = new ArrayList<>();
		int pageNo = 1;
		int totalCount = Integer.MAX_VALUE;

		while ((pageNo - 1) * PAGE_SIZE < totalCount && pageNo <= MAX_PAGES) {
			StoreApiResponseDto response = fetchPage(lon, lat, radiusMeters, pageNo);
			if (response == null || response.body() == null) {
				break;
			}
			if (response.body().items() != null) {
				result.addAll(response.body().items());
			}
			totalCount = response.body().totalCount();
			pageNo++;
		}
		return result;
	}

	private StoreApiResponseDto fetchPage(double lon, double lat, int radiusMeters, int pageNo) {
		try {
			return dataGoKrRestClient.get()
					.uri(uriBuilder -> uriBuilder
							.path("/storeListInRadius")
							.queryParam("serviceKey", appProperties.dataGoKr().key())
							.queryParam("cx", lon)
							.queryParam("cy", lat)
							.queryParam("radius", radiusMeters)
							.queryParam("type", "json")
							.queryParam("numOfRows", PAGE_SIZE)
							.queryParam("pageNo", pageNo)
							.build())
					.retrieve()
					.body(StoreApiResponseDto.class);
		} catch (Exception e) {
			log.warn("상가업소 API 호출 실패 (pageNo={}): {}", pageNo, e.getMessage());
			return null;
		}
	}
}
