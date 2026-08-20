package com.xorwnsho.data_project.query;

import com.xorwnsho.data_project.geo.Location;
import com.xorwnsho.data_project.store.StoreEntity;
import com.xorwnsho.data_project.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 좌표+업종 조건으로 상권 데이터를 조회/집계한다.
 * 챗봇, 탐색기, 비교 화면이 공통으로 사용하는 순수 데이터 집계 로직 (AI 리포트 생성은 별도).
 */
@Service
@RequiredArgsConstructor
public class MarketStatsService {

	public static final int DEFAULT_RADIUS_METERS = 500;
	public static final int MIN_RADIUS_METERS = 100;
	public static final int MAX_RADIUS_METERS = 1000;

	private final StoreService storeService;

	public MarketStats build(Location region, IndustryDictionary.Match industryMatch, int radiusMeters) {
		int clampedRadius = Math.max(MIN_RADIUS_METERS, Math.min(MAX_RADIUS_METERS, radiusMeters));
		List<StoreEntity> nearbyStores = storeService.findNearby(region.lon(), region.lat(), clampedRadius);
		return aggregate(region.name(), industryMatch, nearbyStores);
	}

	private MarketStats aggregate(String regionName, IndustryDictionary.Match industryMatch, List<StoreEntity> stores) {
		var breakdown = stores.stream()
				.collect(Collectors.groupingBy(StoreEntity::getIndsMclsNm, Collectors.counting()));

		long competitorCount = industryMatch == null ? 0 : stores.stream()
				.filter(store -> matchesIndustry(store, industryMatch.targets()))
				.count();

		List<MarketStats.StoreSummary> summaries = stores.stream()
				.map(store -> new MarketStats.StoreSummary(
						store.getBizesNm(), store.getIndsSclsNm(), store.getRdnmAdr(), store.getLon(), store.getLat()))
				.toList();

		String industryLabel = industryMatch == null ? null : industryMatch.label();
		List<String> industryTargets = industryMatch == null ? List.of() : industryMatch.targets();
		return new MarketStats(regionName, industryLabel, industryTargets, stores.size(), (int) competitorCount, breakdown, summaries);
	}

	private boolean matchesIndustry(StoreEntity store, List<String> targets) {
		return targets.stream().anyMatch(target ->
				(store.getIndsSclsNm() != null && store.getIndsSclsNm().contains(target))
						|| (store.getIndsMclsNm() != null && store.getIndsMclsNm().contains(target)));
	}
}
