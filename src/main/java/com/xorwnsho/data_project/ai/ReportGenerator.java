package com.xorwnsho.data_project.ai;

import com.xorwnsho.data_project.query.MarketStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 집계된 상권 통계를 근거로 GPT-4o-mini가 서사형 입지분석 리포트를 작성한다.
 * 프롬프트에 없는 수치를 지어내지 않도록 "주어진 데이터만 근거로" 지시한다.
 */
@Component
@RequiredArgsConstructor
public class ReportGenerator {

	private static final String SYSTEM_PROMPT = """
			너는 대전·세종 지역 예비창업자를 돕는 상권 분석 AI 상담사다.
			아래 사용자 메시지에 대해, 반드시 함께 제공되는 '상권 데이터'의 실제 수치에만 근거해서 답하라.
			데이터에 없는 지역·수치·업종 정보를 지어내지 마라.
			데이터가 부족하면 그 사실을 명시하라.
			경쟁 밀집도, 업종 공백, 리스크 요인을 짚어주는 자연스러운 상담 말투(2~5문단)로 작성하라.
			""";

	private final OpenAiClient openAiClient;

	public String generate(String userMessage, MarketStats stats) {
		String userPrompt = buildUserPrompt(userMessage, stats);
		return openAiClient.chat(SYSTEM_PROMPT, userPrompt);
	}

	private String buildUserPrompt(String userMessage, MarketStats stats) {
		String breakdown = stats.industryBreakdown().entrySet().stream()
				.sorted(Map.Entry.<String, Long>comparingByValue().reversed())
				.limit(10)
				.map(entry -> "- %s: %d개".formatted(entry.getKey(), entry.getValue()))
				.collect(Collectors.joining("\n"));

		return """
				[사용자 질문]
				%s

				[상권 데이터: %s 반경 500m]
				- 전체 상가업소 수: %d개
				- 질의 업종(%s) 경쟁업체 수: %d개
				- 업종별 분포(상위 10개):
				%s
				""".formatted(
				userMessage,
				stats.regionName(),
				stats.totalStoreCount(),
				stats.industryKeyword() == null ? "미지정" : stats.industryKeyword(),
				stats.competitorCount(),
				breakdown
		);
	}
}
