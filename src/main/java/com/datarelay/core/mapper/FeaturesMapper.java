package com.datarelay.core.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.datarelay.core.dto.SchemaDTO.Features;
import com.datarelay.core.entity.Feature;

@Mapper(componentModel = "spring")
public interface FeaturesMapper {
    List<Feature> toEntity(List<Features> features);
}
