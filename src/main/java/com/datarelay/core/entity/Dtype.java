package com.datarelay.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dtype")
public class Dtype {
    
    @Id
    @Column(name = "dtype", columnDefinition = "TEXT")
    private String dtype;

    public Dtype() {

    }
}
