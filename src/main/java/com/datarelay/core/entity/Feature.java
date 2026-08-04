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
@Table("feature")
public class Feature {

    @Id
    @Column("feature_id")
    private UUID featureId;

    // foreign key
    @Column("schema_id")
    private UUID schemaId;

    // foreign key
    @Column("dtype")
    private String dtype;

    @Column("name")
    private String name;

    @Column("position")
    private int position;
}