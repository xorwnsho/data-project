package com.xorwnsho.data_project.geo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Nominatim(OSM) 지오코딩 결과 캐시. 동일 질의어를 재조회할 때 외부 호출을 피한다.
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GeocodeCache {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String query;

	private Double lon;
	private Double lat;

	public GeocodeCache(String query, Double lon, Double lat) {
		this.query = query;
		this.lon = lon;
		this.lat = lat;
	}
}
