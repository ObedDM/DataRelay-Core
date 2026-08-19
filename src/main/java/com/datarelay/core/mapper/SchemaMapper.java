package com.datarelay.core.mapper;

import org.mapstruct.Mapper;

import com.datarelay.core.dto.SchemaDTO.Schema;
import com.datarelay.core.entity.DatasetSchema;

@Mapper(componentModel = "spring")
public interface SchemaMapper {
    DatasetSchema toEntity(Schema dto);
}

