package com.datarelay.core.repository.sql;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.datarelay.core.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {

}