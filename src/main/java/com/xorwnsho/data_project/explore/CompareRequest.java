package com.xorwnsho.data_project.explore;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CompareRequest(@Valid @NotNull Target a, @Valid @NotNull Target b) {

	public record Target(@NotBlank String region, String industry) {
	}
}
