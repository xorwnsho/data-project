package com.xorwnsho.data_project.store;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StoreItemDto(
		@JsonProperty("bizesId") String bizesId,
		@JsonProperty("bizesNm") String bizesNm,
		@JsonProperty("brchNm") String brchNm,
		@JsonProperty("indsLclsNm") String indsLclsNm,
		@JsonProperty("indsMclsNm") String indsMclsNm,
		@JsonProperty("indsSclsNm") String indsSclsNm,
		@JsonProperty("ctprvnNm") String ctprvnNm,
		@JsonProperty("signguNm") String signguNm,
		@JsonProperty("adongNm") String adongNm,
		@JsonProperty("ldongNm") String ldongNm,
		@JsonProperty("lnoAdr") String lnoAdr,
		@JsonProperty("rdnmAdr") String rdnmAdr,
		@JsonProperty("lon") Double lon,
		@JsonProperty("lat") Double lat
) {
}
