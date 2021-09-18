package com.nautilus.sistema_001.api.services;

import com.nautilus.sistema_001.api.entities.UserSaldo;

import java.util.Optional;

public interface UserSaldoService {

    UserSaldo persist(UserSaldo userSaldo);

    Optional<UserSaldo> findById(Long id);

    Optional<UserSaldo> findByIdUsuario(Long id);

    void remove(Long id);
}
