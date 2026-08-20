package com.xorwnsho.data_project.query;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 대전·세종 주요 행정동 좌표 사전 (MVP 하드코딩).
 * 추후 정식 geocoding API로 대체 가능.
 */
@Component
public class RegionDictionary {

	public record Region(String name, double lon, double lat) {
	}

	private static final List<Region> REGIONS = List.of(
			new Region("둔산동", 127.3845, 36.3504),
			new Region("관저동", 127.3491, 36.3033),
			new Region("탄방동", 127.3921, 36.3421),
			new Region("노은동", 127.3283, 36.3699),
			new Region("봉명동", 127.3452, 36.3616),
			new Region("궁동", 127.3672, 36.3629),
			new Region("은행동", 127.4288, 36.3253),
			new Region("대흥동", 127.4327, 36.3268),
			new Region("가양동", 127.4344, 36.3315),
			new Region("판암동", 127.4487, 36.3184),
			new Region("오정동", 127.4218, 36.3684),
			new Region("신탄진동", 127.4166, 36.4308),
			new Region("나성동", 127.2827, 36.4870),
			new Region("도담동", 127.2591, 36.5106),
			new Region("조치원읍", 127.2975, 36.6008)
	);

	/** 메시지에 등장하는 지역명 중 가장 긴 것을 우선 매칭한다. */
	public Optional<Region> match(String message) {
		return REGIONS.stream()
				.filter(region -> message.contains(region.name()))
				.max((a, b) -> Integer.compare(a.name().length(), b.name().length()));
	}
}
