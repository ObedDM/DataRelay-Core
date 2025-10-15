package com.datarelay.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "columns",
    uniqueConstraints = @UniqueConstraint(columnNames = {"schema_id, name"}))
public class Columns {
 
    public Columns() {

    }

    @ManyToOne
    @JoinColumn(
        name = "schema_id",
        foreignKey = @ForeignKey(name = "FK_COLUMNS_SCHEMA"),
        nullable = false
        )
    private DatasetSchema schemaId;

    @OneToOne
    @JoinColumn(
        name = "dtype",
        foreignKey = @ForeignKey(name = "FK_COLUMNS_DTYPE")
        )
    private Dtype dtype;

    @Column(name = "name", columnDefinition = "TEXT", nullable = false)
    private String name;

    @Column(name = "position", columnDefinition = "SMALLINT", nullable = false)
    private int position;

}
