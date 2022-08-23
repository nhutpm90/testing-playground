package com.example.testing.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.testing.entity.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

}
