package com.xorwnsho.data_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DataProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataProjectApplication.class, args);
	}

}
