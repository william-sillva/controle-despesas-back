package com.nautilus.sistema_001.api.services;

import com.nautilus.sistema_001.api.entities.Users;

import java.util.Optional;

public interface UsersService {

    Users persist(Users users);

    Optional<Users> findById(Long id);

    Optional<Users> findByName(String name);

    Optional<Users> findByEmail(String email);

    void remove(Long id);
}
