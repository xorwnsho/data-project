package com.xorwnsho.data_project.store;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreEntity {

	@Id
	private String bizesId;

	private String bizesNm;
	private String indsLclsNm;
	private String indsMclsNm;
	private String indsSclsNm;
	private String signguNm;
	private String adongNm;
	private String rdnmAdr;
	private String lnoAdr;
	private Double lon;
	private Double lat;
	private LocalDateTime updatedAt;

	public static StoreEntity from(StoreItemDto dto) {
		return new StoreEntity(
				dto.bizesId(),
				dto.bizesNm(),
				dto.indsLclsNm(),
				dto.indsMclsNm(),
				dto.indsSclsNm(),
				dto.signguNm(),
				dto.adongNm(),
				dto.rdnmAdr(),
				dto.lnoAdr(),
				dto.lon(),
				dto.lat(),
				LocalDateTime.now()
		);
	}
}
