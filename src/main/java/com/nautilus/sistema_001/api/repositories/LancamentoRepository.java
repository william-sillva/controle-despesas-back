package com.nautilus.sistema_001.api.repositories;

import com.nautilus.sistema_001.api.entities.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
