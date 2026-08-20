package com.xorwnsho.data_project.explore;

import com.xorwnsho.data_project.geo.Location;
import com.xorwnsho.data_project.query.IndustryDictionary;
import com.xorwnsho.data_project.query.MarketStats;
import com.xorwnsho.data_project.query.MarketStatsService;
import com.xorwnsho.data_project.query.RegionDictionary;
import com.xorwnsho.data_project.query.RegionResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * AI 리포트 없이 지역/업종 조건으로 상권 데이터를 바로 조회하는 API.
 * 상권 지도 탐색기, 비교 화면의 지역/업종 드롭다운이 사용한다.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExploreController {

	private final RegionDictionary regionDictionary;
	private final IndustryDictionary industryDictionary;
	private final RegionResolver regionResolver;
	private final MarketStatsService marketStatsService;

	@GetMapping("/regions")
	public List<String> regions() {
		return regionDictionary.names();
	}

	@GetMapping("/industries")
	public List<String> industries() {
		return industryDictionary.labels();
	}

	@GetMapping("/explore")
	public MarketStats explore(
			@RequestParam String region,
			@RequestParam(required = false) String industry,
			@RequestParam(required = false, defaultValue = "500") int radius
	) {
		Location location = regionResolver.resolve(region)
				.orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "지역을 인식하지 못했습니다: " + region));

		IndustryDictionary.Match industryMatch = (industry == null || industry.isBlank())
				? null
				: industryDictionary.findByLabel(industry).orElse(null);

		return marketStatsService.build(location, industryMatch, radius);
	}
}
