package com.xorwnsho.data_project.store;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StoreApiResponseDto(
		@JsonProperty("header") Header header,
		@JsonProperty("body") Body body
) {
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Header(
			@JsonProperty("resultCode") String resultCode,
			@JsonProperty("resultMsg") String resultMsg
	) {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Body(
			@JsonProperty("items") List<StoreItemDto> items,
			@JsonProperty("numOfRows") int numOfRows,
			@JsonProperty("pageNo") int pageNo,
			@JsonProperty("totalCount") int totalCount
	) {
	}
}
