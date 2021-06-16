package com.nautilus.sistema_001.api.services.impl;

import com.nautilus.sistema_001.api.entities.Users;
import com.nautilus.sistema_001.api.repositories.UserRepository;
import com.nautilus.sistema_001.api.services.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UsersServiceImpl implements UsersService {

    private static final Logger log = LoggerFactory.getLogger(UsersServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public Users persist(Users users) {
        log.info("Persistindo usuário {}", users);
        return this.userRepository.save(users);
    }

    @Override
    public Optional<Users> findById(Long id) {
        log.info("Buscando usuário por id {}", id);
        return this.userRepository.findById(id);
    }

    @Override
    public Optional<Users> findByName(String name) {
        log.info("Buscando usuário por nome {}", name);
        return Optional.ofNullable(this.userRepository.findByName(name));
    }

    @Override
    public Optional<Users> findByEmail(String email) {
        log.info("Buscando usuário por email {}", email);
        return Optional.ofNullable(this.userRepository.findByEmail(email));
    }

    @Override
    public void remove(Long id) {
        log.info("Removendo usuário por id {}", id);
        this.userRepository.deleteById(id);
    }
}
