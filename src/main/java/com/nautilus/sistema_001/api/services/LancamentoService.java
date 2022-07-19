package com.nautilus.sistema_001.api.services;

import com.nautilus.sistema_001.api.dtos.LancamentoPagDto;
import com.nautilus.sistema_001.api.entities.Lancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface LancamentoService {

    Lancamento persist(Lancamento lancamento);

    Optional<Lancamento> findById(Long id);

    void remove(Long id);

    Page<Lancamento> findByUserIdPag(LancamentoPagDto lancPagDto, PageRequest pageRequest);

    List<Lancamento> findDataGraphicByUserId(Long userId, Date dtRef);

    List<Lancamento> findDataBetweencByUserId(Long userId, Date dtIni, Date dtFin);
}
