package com.xorwnsho.data_project.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChatCompletionResponse(List<Choice> choices) {

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Choice(ChatMessage message) {
	}
}
