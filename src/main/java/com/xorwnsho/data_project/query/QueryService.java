package com.xorwnsho.data_project.query;

import com.xorwnsho.data_project.ai.ReportGenerator;
import com.xorwnsho.data_project.store.StoreEntity;
import com.xorwnsho.data_project.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class QueryService {

	private static final int RADIUS_METERS = 500;

	private final RegionDictionary regionDictionary;
	private final IndustryDictionary industryDictionary;
	private final StoreService storeService;
	private final ReportGenerator reportGenerator;

	public QueryResponse handle(String message) {
		RegionDictionary.Region region = regionDictionary.match(message)
				.orElseThrow(() -> new ResponseStatusException(BAD_REQUEST,
						"메시지에서 지역을 인식하지 못했습니다. 예: 둔산동, 노은동, 나성동 등 동 이름을 포함해 질문해주세요."));

		IndustryDictionary.Match industryMatch = industryDictionary.match(message).orElse(null);

		List<StoreEntity> nearbyStores = storeService.findNearby(region.lon(), region.lat(), RADIUS_METERS);
		MarketStats stats = aggregate(region.name(), industryMatch, nearbyStores);

		String report = reportGenerator.generate(message, stats);
		return new QueryResponse(report, stats);
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
		return new MarketStats(regionName, industryLabel, stores.size(), (int) competitorCount, breakdown, summaries);
	}

	private boolean matchesIndustry(StoreEntity store, List<String> targets) {
		return targets.stream().anyMatch(target ->
				(store.getIndsSclsNm() != null && store.getIndsSclsNm().contains(target))
						|| (store.getIndsMclsNm() != null && store.getIndsMclsNm().contains(target)));
	}
}
