package com.xorwnsho.data_project.query;

import com.xorwnsho.data_project.ai.ReportGenerator;
import com.xorwnsho.data_project.geo.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class QueryService {

	private final RegionResolver regionResolver;
	private final IndustryDictionary industryDictionary;
	private final MarketStatsService marketStatsService;
	private final ReportGenerator reportGenerator;

	public QueryResponse handle(String message) {
		Location region = regionResolver.resolve(message)
				.orElseThrow(() -> new ResponseStatusException(BAD_REQUEST,
						"메시지에서 지역을 인식하지 못했습니다. 대전·세종의 동/읍/면 이름을 포함해 질문해주세요. (예: 둔산동, 노은동, 나성동)"));

		IndustryDictionary.Match industryMatch = industryDictionary.match(message).orElse(null);

		MarketStats stats = marketStatsService.build(region, industryMatch, MarketStatsService.DEFAULT_RADIUS_METERS);
		String report = reportGenerator.generate(message, stats);
		return new QueryResponse(report, stats);
	}
}
