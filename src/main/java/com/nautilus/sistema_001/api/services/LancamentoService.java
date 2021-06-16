package com.nautilus.sistema_001.api.services;

import com.nautilus.sistema_001.api.entities.Lancamento;

import java.util.Optional;

public interface LancamentoService {

    Lancamento persist(Lancamento lancamento);

    Optional<Lancamento> findById(Long id);

    void remove(Long id);
}
