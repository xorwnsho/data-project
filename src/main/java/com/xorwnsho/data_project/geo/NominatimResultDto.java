package com.xorwnsho.data_project.geo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NominatimResultDto(
		@JsonProperty("display_name") String displayName,
		@JsonProperty("lat") String lat,
		@JsonProperty("lon") String lon
) {
}
