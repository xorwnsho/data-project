package com.xorwnsho.data_project.query;

import jakarta.validation.constraints.NotBlank;

public record QueryRequest(@NotBlank String message) {
}
