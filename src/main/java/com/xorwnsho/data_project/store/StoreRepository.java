package com.xorwnsho.data_project.store;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<StoreEntity, String> {

	List<StoreEntity> findByLonBetweenAndLatBetween(double lonMin, double lonMax, double latMin, double latMax);
}
