package com.datarelay.core.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SchemaDTO(
    @NotNull @Valid Schema schema,
    @Valid List<Dimensions> dimensions,
    @NotEmpty @Valid List<Features> features
) {
    public record Schema(
        @NotBlank @Size(max=25) String name,
        String description
    ) {}

    public record Features(
        @NotBlank @Size(max=25) String name,
        @NotBlank String dtype,
        @NotNull Integer position
    ) {}

    public record Dimensions(
        @NotBlank @Size(max=25) String name,
        @NotBlank String dtype,
        @NotNull Integer axisOrder
    ) {}
}

