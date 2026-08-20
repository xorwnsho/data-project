package com.xorwnsho.data_project.query;

import com.xorwnsho.data_project.geo.GeocodingService;
import com.xorwnsho.data_project.geo.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 지역명(문장이든 단일 지명이든) -> 좌표. 하드코딩 사전을 먼저 보고, 없으면 지오코딩으로 폴백한다.
 * 챗봇 질의, 탐색기, 비교 화면이 공통으로 사용한다.
 */
@Service
@RequiredArgsConstructor
public class RegionResolver {

	private final RegionDictionary regionDictionary;
	private final GeocodingService geocodingService;

	public Optional<Location> resolve(String text) {
		return regionDictionary.match(text).or(() -> geocodingService.resolve(text));
	}
}
