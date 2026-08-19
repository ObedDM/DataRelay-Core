package com.datarelay.core.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDTO(
    @NotBlank @Size(max=20) @Valid String username,
    @NotBlank @Valid String password
) {}
