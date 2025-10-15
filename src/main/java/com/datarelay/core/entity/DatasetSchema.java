package com.datarelay.core.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "dataset_schema",
    uniqueConstraints = @UniqueConstraint(columnNames = {"name", "user_id"})
)
public class DatasetSchema {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "schema_id")
    private UUID schemaId;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_SCHEMA_USERS"))
    private Users userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public DatasetSchema() {

    }

}
