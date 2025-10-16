package com.datarelay.core.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "columns",
    uniqueConstraints = @UniqueConstraint(columnNames = {"schema_id", "name"}))
public class Columns {

    @EmbeddedId
    private ColumnsId id;
 
    public Columns() {

    }

    @MapsId("schemaId")
    @ManyToOne
    @JoinColumn(
        name = "schema_id",
        foreignKey = @ForeignKey(name = "FK_COLUMNS_SCHEMA"),
        nullable = false
        )
    private DatasetSchema schema;

    @OneToOne
    @JoinColumn(
        name = "dtype",
        foreignKey = @ForeignKey(name = "FK_COLUMNS_DTYPE")
        )
    private Dtype dtype;

    @Column(name = "position", columnDefinition = "SMALLINT", nullable = false)
    private int position;

    public ColumnsId getId() {
        return id;
    }

    public void setId(ColumnsId id) {
        this.id = id;
    }

    public DatasetSchema getSchema() {
        return schema;
    }

    public void setSchema(DatasetSchema schema) {
        this.schema = schema;
    }

    public Dtype getDtype() {
        return dtype;
    }

    public void setDtype(Dtype dtype) {
        this.dtype = dtype;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Embeddable
    public static class ColumnsId implements Serializable {

        @Column(name = "schema_id", nullable = false)
        private UUID schemaId;
        
        @Column(name = "name", columnDefinition = "TEXT", nullable = false)
        private String name;
        
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public UUID getSchemaId() {
            return schemaId;
        }

        public void setSchemaId(UUID schemaId) {
            this.schemaId = schemaId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ColumnsId that)) return false;
            return Objects.equals(schemaId, that.schemaId) &&
                Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(schemaId, name);
        }

    }
}