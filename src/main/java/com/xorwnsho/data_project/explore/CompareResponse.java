package com.xorwnsho.data_project.explore;

import com.xorwnsho.data_project.query.MarketStats;

public record CompareResponse(MarketStats a, MarketStats b, String comparison) {
}
