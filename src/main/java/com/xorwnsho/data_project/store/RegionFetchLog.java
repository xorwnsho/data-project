package com.xorwnsho.data_project.store;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * (좌표, 반경) 조합별 외부 API 조회 이력.
 * 동일 지점 재조회 시 API 대신 캐싱된 StoreEntity를 사용해 일일 호출 한도를 아낀다.
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegionFetchLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String regionKey;
	private LocalDateTime fetchedAt;

	public RegionFetchLog(String regionKey) {
		this.regionKey = regionKey;
		this.fetchedAt = LocalDateTime.now();
	}
}
