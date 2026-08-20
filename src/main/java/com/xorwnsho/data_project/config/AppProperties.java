package com.xorwnsho.data_project.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(
		Cors cors,
		DataGoKr dataGoKr,
		OpenAi openai
) {
	public record Cors(String allowedOrigin) {
	}

	public record DataGoKr(String key, String baseUrl) {
	}

	public record OpenAi(String key, String baseUrl, String model) {
	}
}
