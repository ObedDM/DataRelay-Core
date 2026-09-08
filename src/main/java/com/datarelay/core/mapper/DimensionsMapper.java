package com.datarelay.core.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.datarelay.core.dto.SchemaDTO.Dimensions;
import com.datarelay.core.entity.Dimension;

@Mapper(componentModel = "spring")
public interface DimensionsMapper {
    List<Dimension> toEntity(List<Dimensions> dimensions);
}
