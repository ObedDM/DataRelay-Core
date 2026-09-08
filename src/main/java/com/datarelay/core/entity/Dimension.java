package com.datarelay.core.entity;

import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("dimension")
public class Dimension {

    @Id
    @Column("dimension_id")
    private UUID dimensionId;

    // foreign key
    @Column("schema_id")
    private UUID schemaId;

    // foreign key
    @Column("dtype")
    private String dtype;

    @Column("name")
    private String name;

    @Column("axis_order")
    private Integer axisOrder;
}