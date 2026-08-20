package com.xorwnsho.data_project.ai;

import com.xorwnsho.data_project.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OpenAiClient {

	private final RestClient openAiRestClient;
	private final AppProperties appProperties;

	public String chat(String systemPrompt, String userPrompt) {
		ChatCompletionRequest request = new ChatCompletionRequest(
				appProperties.openai().model(),
				List.of(
						new ChatMessage("system", systemPrompt),
						new ChatMessage("user", userPrompt)
				),
				0.4
		);

		ChatCompletionResponse response = openAiRestClient.post()
				.uri("/chat/completions")
				.body(request)
				.retrieve()
				.body(ChatCompletionResponse.class);

		if (response == null || response.choices() == null || response.choices().isEmpty()) {
			throw new IllegalStateException("OpenAI 응답이 비어 있습니다.");
		}
		return response.choices().get(0).message().content();
	}
}
