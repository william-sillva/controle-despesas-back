package com.nautilus.sistema_001.api.repositories;

import com.nautilus.sistema_001.api.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface UserRepository extends JpaRepository<Users, Long> {

    @Transactional(readOnly = true)
    Users findByName(String name);

    @Transactional(readOnly = true)
    Users findByEmail(String email);
}
