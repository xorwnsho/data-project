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
}
