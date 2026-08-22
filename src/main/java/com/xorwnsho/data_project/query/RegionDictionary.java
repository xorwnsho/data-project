package com.xorwnsho.data_project.query;

import com.xorwnsho.data_project.geo.Location;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 자주 쓰이는 대전·세종 주요 행정동 좌표 사전 (핫 캐시, 외부 호출 없이 즉시 매칭).
 * 여기 없는 지역명은 {@link com.xorwnsho.data_project.geo.GeocodingService}가 처리한다.
 */
@Component
public class RegionDictionary {

	private static final List<Location> REGIONS = List.of(
			new Location("둔산동", 127.3845, 36.3504),
			new Location("관저동", 127.3491, 36.3033),
			new Location("탄방동", 127.3921, 36.3421),
			new Location("노은동", 127.3283, 36.3699),
			new Location("봉명동", 127.3452, 36.3616),
			new Location("궁동", 127.3672, 36.3629),
			new Location("은행동", 127.4288, 36.3253),
			new Location("대흥동", 127.4327, 36.3268),
			new Location("가양동", 127.4344, 36.3315),
			new Location("판암동", 127.4487, 36.3184),
			new Location("오정동", 127.4218, 36.3684),
			new Location("신탄진동", 127.4166, 36.4308),
			new Location("나성동", 127.2625, 36.4900),
			new Location("도담동", 127.2591, 36.5106),
			new Location("조치원읍", 127.2975, 36.6008),
			// 자주 언급되는 랜드마크 (자연어 질의 대응, 동/읍/면으로 안 끝나는 고유명사라 별도 등록)
			new Location("카이스트", 127.3620, 36.3703),
			new Location("성심당", 127.4273, 36.3277),
			new Location("한밭수목원", 127.3890, 36.3650),
			new Location("엑스포과학공원", 127.3877, 36.3760),
			new Location("충남대학교", 127.3448, 36.3699),
			new Location("한밭대학교", 127.2981, 36.3518),
			new Location("세종호수공원", 127.2762, 36.5021),
			new Location("정부세종청사", 127.2645, 36.5039)
	);

	/** 메시지에 등장하는 지역명 중 가장 긴 것을 우선 매칭한다. */
	public Optional<Location> match(String message) {
		return REGIONS.stream()
				.filter(region -> message.contains(region.name()))
				.max((a, b) -> Integer.compare(a.name().length(), b.name().length()));
	}

	/** 자주 쓰이는 지역 이름 목록 (탐색기/비교 화면 드롭다운 힌트용, 자유 입력도 지오코딩으로 지원됨). */
	public List<String> names() {
		return REGIONS.stream().map(Location::name).toList();
	}
}
