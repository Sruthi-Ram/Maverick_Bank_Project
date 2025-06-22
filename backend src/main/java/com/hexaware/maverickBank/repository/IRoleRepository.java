package com.hexaware.maverickbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hexaware.maverickbank.dto.entity.Role;
@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}