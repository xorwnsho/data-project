package com.xorwnsho.data_project.explore;

import com.xorwnsho.data_project.ai.ReportGenerator;
import com.xorwnsho.data_project.geo.Location;
import com.xorwnsho.data_project.query.IndustryDictionary;
import com.xorwnsho.data_project.query.MarketStats;
import com.xorwnsho.data_project.query.MarketStatsService;
import com.xorwnsho.data_project.query.RegionResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class CompareService {

	private final RegionResolver regionResolver;
	private final IndustryDictionary industryDictionary;
	private final MarketStatsService marketStatsService;
	private final ReportGenerator reportGenerator;

	public CompareResponse compare(CompareRequest request) {
		MarketStats statsA = resolveStats(request.a());
		MarketStats statsB = resolveStats(request.b());
		String comparison = reportGenerator.generateComparison(statsA, statsB);
		return new CompareResponse(statsA, statsB, comparison);
	}

	private MarketStats resolveStats(CompareRequest.Target target) {
		Location location = regionResolver.resolve(target.region())
				.orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "지역을 인식하지 못했습니다: " + target.region()));

		IndustryDictionary.Match industryMatch = (target.industry() == null || target.industry().isBlank())
				? null
				: industryDictionary.findByLabel(target.industry()).orElse(null);

		return marketStatsService.build(location, industryMatch, MarketStatsService.DEFAULT_RADIUS_METERS);
	}
}
