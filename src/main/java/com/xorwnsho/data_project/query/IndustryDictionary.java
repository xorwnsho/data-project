package com.xorwnsho.data_project.query;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 업종 사전 (MVP 하드코딩). 실제 data.go.kr 응답 샘플의 indsSclsNm/indsMclsNm 값을 기준으로 만들었다
 * (예: "카페", "중국집", "생맥주 전문" 등).
 * - match(message): 자연어 문장에서 keywords 중 하나라도 등장하면 매칭 (챗봇용)
 * - findByLabel(label): 드롭다운에서 고른 정확한 라벨로 조회 (탐색기/비교용)
 */
@Component
public class IndustryDictionary {

	public record Match(String label, List<String> targets) {
	}

	private record Entry(String label, List<String> keywords, List<String> targets) {
	}

	private static final List<Entry> ENTRIES = List.of(
			new Entry("카페", List.of("카페", "커피"), List.of("카페")),
			new Entry("치킨", List.of("치킨"), List.of("치킨")),
			new Entry("편의점", List.of("편의점"), List.of("편의점")),
			new Entry("미용실", List.of("미용실", "헤어"), List.of("미용실")),
			new Entry("한식", List.of("한식"), List.of("한식")),
			new Entry("중식", List.of("중식", "중국집"), List.of("중국집")),
			new Entry("일식", List.of("일식"), List.of("일식")),
			new Entry("분식", List.of("분식"), List.of("분식")),
			new Entry("술집/호프", List.of("술집", "호프"), List.of("주점", "생맥주", "호프")),
			new Entry("PC방", List.of("PC방"), List.of("PC방")),
			new Entry("학원", List.of("학원"), List.of("학원")),
			new Entry("병의원", List.of("병원"), List.of("병원", "의원")),
			new Entry("약국", List.of("약국"), List.of("약국")),
			new Entry("부동산", List.of("부동산"), List.of("부동산")),
			new Entry("세탁", List.of("세탁소", "빨래방"), List.of("세탁")),
			new Entry("제과/빵", List.of("베이커리", "빵집"), List.of("빵", "제과"))
	);

	/** 자연어 문장에서 첫 번째로 매칭되는 업종을 찾는다 (챗봇 질의 파싱용). */
	public Optional<Match> match(String message) {
		return ENTRIES.stream()
				.filter(entry -> entry.keywords().stream().anyMatch(message::contains))
				.findFirst()
				.map(entry -> new Match(entry.label(), entry.targets()));
	}

	/** 드롭다운 등에서 선택된 정확한 라벨로 조회한다 (탐색기/비교 화면용). */
	public Optional<Match> findByLabel(String label) {
		return ENTRIES.stream()
				.filter(entry -> entry.label().equals(label))
				.findFirst()
				.map(entry -> new Match(entry.label(), entry.targets()));
	}

	public List<String> labels() {
		return ENTRIES.stream().map(Entry::label).toList();
	}
}
