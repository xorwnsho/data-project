package com.xorwnsho.data_project.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

	private final AppProperties appProperties;

	@Bean
	public RestClient dataGoKrRestClient() {
		return RestClient.builder()
				.baseUrl(appProperties.dataGoKr().baseUrl())
				.build();
	}

	@Bean
	public RestClient openAiRestClient() {
		return RestClient.builder()
				.baseUrl(appProperties.openai().baseUrl())
				.defaultHeader("Authorization", "Bearer " + appProperties.openai().key())
				.defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
				.build();
	}

	@Bean
	public RestClient nominatimRestClient() {
		return RestClient.builder()
				.baseUrl("https://nominatim.openstreetmap.org")
				.defaultHeader("User-Agent", "daejeon-sejong-sangkwon-ai/0.1 (public-data hackathon submission)")
				.build();
	}
}
