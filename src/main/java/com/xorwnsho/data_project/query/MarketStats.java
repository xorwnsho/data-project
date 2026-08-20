package com.xorwnsho.data_project.query;

import java.util.List;
import java.util.Map;

public record MarketStats(
		String regionName,
		String industryKeyword,
		List<String> industryTargets,
		int totalStoreCount,
		int competitorCount,
		Map<String, Long> industryBreakdown,
		List<StoreSummary> stores
) {
	public record StoreSummary(String name, String industry, String address, double lon, double lat) {
	}
}
