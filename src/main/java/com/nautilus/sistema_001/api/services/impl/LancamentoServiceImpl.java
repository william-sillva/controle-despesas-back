package com.nautilus.sistema_001.api.services.impl;

import com.nautilus.sistema_001.api.dtos.LancamentoPagDto;
import com.nautilus.sistema_001.api.entities.Lancamento;
import com.nautilus.sistema_001.api.repositories.LancamentoRepository;
import com.nautilus.sistema_001.api.services.LancamentoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.Optional;

public class LancamentoServiceImpl implements LancamentoService {

    private static final Logger log = LoggerFactory.getLogger(LancamentoServiceImpl.class);

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Override
    public Lancamento persist(Lancamento lancamento) {
        log.info("Persistindo lancamento {}", lancamento);
        return this.lancamentoRepository.save(lancamento);
    }

    @Override
    public Optional<Lancamento> findById(Long id) {
        log.info("Buscando lancamento por id {}", id);
        return this.lancamentoRepository.findById(id);
    }

    @Override
    public void remove(Long id) {
        log.info("Removendo lancamento {}", id);
        this.lancamentoRepository.deleteById(id);
    }

    @Override
    public Page<Lancamento> findByUserIdPag(LancamentoPagDto lancPagDto, PageRequest pageRequest) {
        log.info("Recuperando Lançamentos paginado por usuário ID {}, dtIni {}, dtFin {}",
                lancPagDto.getUserId(), lancPagDto.getDtInit(), lancPagDto.getDtFin());
        return this.lancamentoRepository.findByFilter(
                lancPagDto.getUserId(), lancPagDto.getDateIn(), lancPagDto.getDateFn(), pageRequest);
    }
}
