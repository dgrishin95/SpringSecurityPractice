package com.mysite.spring.security.repositories;

import com.mysite.spring.security.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
