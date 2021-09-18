package com.nautilus.sistema_001.api.services.impl;

import com.nautilus.sistema_001.api.entities.UserSaldo;
import com.nautilus.sistema_001.api.repositories.UserSaldoRepository;
import com.nautilus.sistema_001.api.services.UserSaldoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserSaldoServiceImpl implements UserSaldoService {

    private static final Logger log = LoggerFactory.getLogger(UserSaldoServiceImpl.class);

    @Autowired
    private UserSaldoRepository userSaldoRepository;

    @Override
    public UserSaldo persist(UserSaldo userSaldo) {
        log.info("Persistindo saldo do usuário {}", userSaldo);
        return this.userSaldoRepository.save(userSaldo);
    }

    @Override
    public Optional<UserSaldo> findById(Long id) {
        log.info("Buscando saldo do usuário por id {}", id);
        return this.userSaldoRepository.findById(id);
    }

    @Override
    public Optional<UserSaldo> findByIdUsuario(Long id) {
        log.info("Buscando saldo do usuário por idUsuario {}", id);
        return this.userSaldoRepository.findByUserId(id);
    }

    @Override
    public void remove(Long id) {
        log.info("Removendo saldo do usuário por id {}", id);
        this.userSaldoRepository.deleteById(id);
    }
}
