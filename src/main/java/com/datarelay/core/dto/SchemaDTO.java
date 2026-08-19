package com.datarelay.core.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SchemaDTO(
    @NotNull @Valid Schema schema,
    @NotEmpty @Valid List<Feature> features
) {
    public record Schema(
        @NotBlank @Size(max=25) String name,
        String description
    ) {}

    public record Feature(
        @NotBlank @Size(max=25) String name,
        @NotBlank String dtype,
        String position
    ) {}
}

