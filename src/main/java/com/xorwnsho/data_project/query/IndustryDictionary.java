package com.xorwnsho.data_project.query;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 사용자 발화 키워드 -> 상권업종 소분류명(indsSclsNm) 매칭 대상 (MVP 하드코딩).
 * 실제 data.go.kr 응답 샘플의 indsSclsNm 값을 기준으로 만들었다 (예: "카페", "중국집", "생맥주 전문" 등).
 */
@Component
public class IndustryDictionary {

	public record Match(String label, List<String> targets) {
	}

	private static final Map<String, Match> KEYWORD_TO_MATCH = new LinkedHashMap<>();

	static {
		put("카페", "카페", "카페");
		put("커피", "카페", "카페");
		put("치킨", "치킨", "치킨");
		put("편의점", "편의점", "편의점");
		put("미용실", "미용실", "미용실");
		put("헤어", "미용실", "미용실");
		put("한식", "한식", "한식");
		put("중식", "중식", "중국집");
		put("중국집", "중식", "중국집");
		put("일식", "일식", "일식");
		put("분식", "분식", "분식");
		put("술집", "술집/호프", "주점", "생맥주", "호프");
		put("호프", "술집/호프", "주점", "생맥주", "호프");
		put("PC방", "PC방", "PC방");
		put("학원", "학원", "학원");
		put("병원", "병의원", "병원", "의원");
		put("약국", "약국", "약국");
		put("부동산", "부동산", "부동산");
		put("세탁소", "세탁", "세탁");
		put("빨래방", "세탁", "세탁");
		put("베이커리", "제과/빵", "빵", "제과");
		put("빵집", "제과/빵", "빵", "제과");
	}

	private static void put(String keyword, String label, String... targets) {
		KEYWORD_TO_MATCH.put(keyword, new Match(label, List.of(targets)));
	}

	public Optional<Match> match(String message) {
		return KEYWORD_TO_MATCH.entrySet().stream()
				.filter(entry -> message.contains(entry.getKey()))
				.map(Map.Entry::getValue)
				.findFirst();
	}
}
