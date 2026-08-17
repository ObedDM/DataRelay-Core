package com.datarelay.core.entity;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @Column("user_id")
    private UUID userId;

    @Column("username")
    private String username;

    @Column("password")
    private String password;

    @CreatedDate
    @Column("created_at")
    private Instant createdAt;
}
