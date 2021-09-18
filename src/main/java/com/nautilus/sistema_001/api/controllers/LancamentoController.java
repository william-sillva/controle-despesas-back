package com.nautilus.sistema_001.api.controllers;

import com.nautilus.sistema_001.api.dtos.EnumDto;
import com.nautilus.sistema_001.api.dtos.LancamentoDto;
import com.nautilus.sistema_001.api.dtos.LancamentoPagDto;
import com.nautilus.sistema_001.api.entities.Lancamento;
import com.nautilus.sistema_001.api.entities.UserSaldo;
import com.nautilus.sistema_001.api.entities.Users;
import com.nautilus.sistema_001.api.enums.DespesaTypeEnum;
import com.nautilus.sistema_001.api.enums.LancamentoTypeEnum;
import com.nautilus.sistema_001.api.response.Response;
import com.nautilus.sistema_001.api.services.LancamentoService;
import com.nautilus.sistema_001.api.services.UserSaldoService;
import com.nautilus.sistema_001.api.services.UsersService;
import com.nautilus.sistema_001.api.services.impl.LancamentoServiceImpl;
import com.nautilus.sistema_001.api.services.impl.UserSaldoServiceImpl;
import com.nautilus.sistema_001.api.services.impl.UsersServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/lancamentos")
@CrossOrigin(origins = "*")
public class LancamentoController {

    private static final Logger log = LoggerFactory.getLogger(LancamentoController.class);

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private UserSaldoService userSaldoService;

    @Configuration
    @Import({LancamentoServiceImpl.class, UsersServiceImpl.class, UserSaldoServiceImpl.class})
    static class Config {}

    @Value("${paginacao.qtd_por_pagina}")
    private int qtdPorPagina;

    @GetMapping("/despesas")
    public ResponseEntity<Response<Collection<EnumDto>>> getTypesDespesas() {
        log.info("Recuperando Tipos de Despesas:");
        Response<Collection<EnumDto>> response = new Response<>();

        Collection<EnumDto> typeList = Stream.of(DespesaTypeEnum.values())
                .map(d -> new EnumDto(d.getChave(), d.getDescricao()))
                .collect(Collectors.toList());

        response.setData(typeList);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}/{userId}")
    public ResponseEntity<Response<LancamentoDto>> getLancamento(@PathVariable("id") Long id,
                                                             @PathVariable("userId") Long userId) {
        log.info("Recuperando lançamento pelo ID: {} e usuário: {}", id, userId);
        Response<LancamentoDto> response = new Response<>();
        Optional<Users> user = usersService.findById(userId);
        Optional<Lancamento> lancamento = lancamentoService.findById(id);

        this.validateFindLaunchUser(user, lancamento, response);

        if (!response.getErrors().isEmpty()) {
            log.error("Erro ao validar dados de recuperação de lancamento: {}", response.getErrors());
            return ResponseEntity.badRequest().body(response);
        }

        LancamentoDto lancamentoDto = this.convertEntityToDto(lancamento.get());
        response.setData(lancamentoDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<Response<UserSaldo>> getSaldoUsua(@PathVariable("userId") Long userId) {
        log.info("Recuperando saldo do usuario pelo ID_USER: {}", userId);
        Response<UserSaldo> response = new Response<>();
        Optional<UserSaldo> userSaldoOpt = this.userSaldoService.findByIdUsuario(userId);
        UserSaldo userSaldo = userSaldoOpt.get();

        if (userSaldoOpt.isEmpty()) {
            userSaldo.setSaldo(new BigDecimal(0L));
        }

        response.setData(userSaldo);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/usuario")
    public ResponseEntity<Response<Page<LancamentoDto>>> listForFilter(@RequestBody LancamentoPagDto lancPagDto)
    throws ParseException {

        log.info("Recuperando lançamentos por ID do usuário: {}, página: {}", lancPagDto.getUserId(), lancPagDto.getPag());
        Response<Page<LancamentoDto>> response = new Response<>();

        PageRequest pageRequest = PageRequest.of(
                lancPagDto.getPag(),
                this.qtdPorPagina,
                Sort.Direction.valueOf(lancPagDto.getDir()),
                lancPagDto.getOrd());
        lancPagDto.setDateIn(this.sdf.parse(lancPagDto.getDtInit()));
        lancPagDto.setDateFn(this.sdf.parse(lancPagDto.getDtFin()));
        Page<Lancamento> lancamentos = this.lancamentoService.findByUserIdPag(lancPagDto, pageRequest);
        Page<LancamentoDto> lancamentosDto = lancamentos.map(this::convertEntityToDto);

        response.setData(lancamentosDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Response<LancamentoDto>> addLancamento(@Valid @RequestBody LancamentoDto lancamentoDto,
                                                                 BindingResult result) throws ParseException {

        log.info("Adicionando lancamento para o usuário: {}", lancamentoDto.getUsuarioId());
        Response<LancamentoDto> response = new Response<>();
        Optional<Users> user = usersService.findById(lancamentoDto.getUsuarioId());

        this.validateExistData(lancamentoDto, user, result);

        if (result.hasErrors()) {
            log.error("Erro ao validar dados do lancamento: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        handleUserSaldo(lancamentoDto, user);
        Lancamento lancamento = this.convertDtoToEntity(lancamentoDto, user);
        this.lancamentoService.persist(lancamento);
        response.setData(this.convertEntityToDto(lancamento));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{id}/{userId}")
    public ResponseEntity<Response<String>> removeLancamento(@PathVariable("id") Long id,
                                                             @PathVariable("userId") Long userId) {
        log.info("Removendo lançamento pelo ID: {} e usuário: {}", id, userId);
        Response<String> response = new Response<>();
        Optional<Users> user = usersService.findById(userId);
        Optional<Lancamento> lancamento = lancamentoService.findById(id);

        this.validateDeleteLaunchUser(user, lancamento, response);

        if (!response.getErrors().isEmpty()) {
            log.error("Erro ao validar dados da remoção do lancamento: {}", response.getErrors());
            return ResponseEntity.badRequest().body(response);
        }

        LancamentoDto lancamentoDto = this.convertEntityToDto(lancamento.get());
        handleReverseUserSaldo(lancamentoDto);
        this.lancamentoService.remove(id);
        return ResponseEntity.ok(response);
    }

    private void handleUserSaldo(LancamentoDto lancamentoDto, Optional<Users> user) {
        BigDecimal value = new BigDecimal(lancamentoDto.getValor());
        Optional<UserSaldo> userSaldo = this.userSaldoService.findByIdUsuario(lancamentoDto.getUsuarioId());

        if (userSaldo.isEmpty()) {
            persistFirstUserSaldo(lancamentoDto, user, value);
        } else {
            handleReverseUserSaldo(lancamentoDto);
            BigDecimal saldoPersist = getSaldoUpdated(lancamentoDto, value, userSaldo);
            userSaldo.get().setSaldo(saldoPersist);
            this.userSaldoService.persist(userSaldo.get());
        }
    }

    /**
     * ESTORNAR SALDO
     */
    private void handleReverseUserSaldo(LancamentoDto lancamentoDto) {
        // se tem id - alteracao/exclusao
        if (lancamentoDto.getId().isPresent()) {
           Optional<Lancamento> lancamentoOpt = this.lancamentoService.findById(lancamentoDto.getId().get());
           if (lancamentoOpt.isPresent()) {
               BigDecimal saldoPersist;
               Lancamento lancamento = lancamentoOpt.get();
               Optional<UserSaldo> userSaldoReverse = this.userSaldoService.findByIdUsuario(lancamentoDto.getUsuarioId());
               if (LancamentoTypeEnum.D.equals(lancamento.getTpLancamento())) {
                   saldoPersist = userSaldoReverse.get().getSaldo().add(lancamento.getValor());
               } else {
                   saldoPersist = userSaldoReverse.get().getSaldo().subtract(lancamento.getValor());
               }

               userSaldoReverse.get().setSaldo(saldoPersist);
               this.userSaldoService.persist(userSaldoReverse.get());
           }
        }
    }

    private BigDecimal getSaldoUpdated(LancamentoDto lancamentoDto, BigDecimal value, Optional<UserSaldo> userSaldo) {
        BigDecimal saldoPersist;
        if (LancamentoTypeEnum.D.toString().equals(lancamentoDto.getTpLancamento())) {
            saldoPersist = userSaldo.get().getSaldo().subtract(value);
        } else {
            saldoPersist = userSaldo.get().getSaldo().add(value);
        }

        return saldoPersist;
    }

    private void persistFirstUserSaldo(LancamentoDto lancamentoDto, Optional<Users> user, BigDecimal value) {
        BigDecimal saldoPersist;
        if (LancamentoTypeEnum.D.toString().equals(lancamentoDto.getTpLancamento())) {
            saldoPersist = value.multiply(new BigDecimal(-1L));
        } else {
            saldoPersist = value;
        }

        UserSaldo us = new UserSaldo();
        us.setUser(user.get());
        us.setSaldo(saldoPersist);
        this.userSaldoService.persist(us);
    }

    private void validateExistData(LancamentoDto lancamentoDto, Optional<Users> user, BindingResult result) {

        if (!user.isPresent()) {
            result.addError(new ObjectError("usuario", "Usuário inexistente."));
        }

        if (!this.getLancamentoTypeEnum(lancamentoDto.getTpLancamento()).isPresent()) {
            result.addError(new ObjectError("tipoLancamento", "Tipo de Lançamento inexistente."));
        }

        // valida somente para tipo D = Despesa
        if (LancamentoTypeEnum.D.toString().equals(lancamentoDto.getTpLancamento())) {
            if (!this.getDespesaTypeEnum(lancamentoDto.getTpDespesa()).isPresent()) {
                result.addError(new ObjectError("tipoDespesa", "Tipo de Despesa inexistente."));
            }
        }
    }

    private void validateDeleteLaunchUser(Optional<Users> user, Optional<Lancamento> lancamento,
                                          Response<String> response) {

        if (!user.isPresent()) {
            response.getErrors().add("Usuário inexistente.");
        }

        if (!lancamento.isPresent()) {
            response.getErrors().add("Lançamento inexistente.");
        }

        if (user.isPresent() && lancamento.isPresent()) {
            if (!user.get().getId().equals(lancamento.get().getUser().getId())) {
                response.getErrors().add("Usuário não tem permissão para remover este registro.");
            }
        }
    }

    private void validateFindLaunchUser(Optional<Users> user, Optional<Lancamento> lancamento,
                                          Response<LancamentoDto> response) {

        if (!user.isPresent()) {
            response.getErrors().add("Usuário inexistente.");
        }

        if (!lancamento.isPresent()) {
            response.getErrors().add("Lançamento inexistente.");
        }

        if (user.isPresent() && lancamento.isPresent()) {
            if (!user.get().getId().equals(lancamento.get().getUser().getId())) {
                response.getErrors().add("Registro não localizado.");
            }
        }
    }

    private Optional<LancamentoTypeEnum> getLancamentoTypeEnum(String tp) {
        return Arrays.stream(LancamentoTypeEnum.values())
                .filter(lte -> lte.toString().equals(tp)).findAny();
    }

    private Optional<DespesaTypeEnum> getDespesaTypeEnum(String tp) {
        return Arrays.stream(DespesaTypeEnum.values())
                .filter(dte -> dte.getChave().equals(tp)).findAny();
    }

    private Lancamento convertDtoToEntity(LancamentoDto dto, Optional<Users> user) throws ParseException {
        Lancamento lancamento = new Lancamento();

        if (!dto.getId().isEmpty()) {
            lancamento.setId(dto.getId().get());
        }
        lancamento.setUser(user.get());
        lancamento.setDtLancamento(this.sdf.parse(dto.getDtLancamento()));
        lancamento.setValor(new BigDecimal(dto.getValor()));
        lancamento.setTpLancamento(this.getLancamentoTypeEnum(dto.getTpLancamento()).get());
        lancamento.setTpDespesa(this.getDespesaTypeEnum(dto.getTpDespesa()).isPresent()
                ? this.getDespesaTypeEnum(dto.getTpDespesa()).get()
                : null);
        lancamento.setObservacao(dto.getObservacao());

        return lancamento;
    }

    private LancamentoDto convertEntityToDto(Lancamento lancamento) {
        LancamentoDto dto = new LancamentoDto();
        dto.setId(Optional.of(lancamento.getId()));
        dto.setUsuarioId(lancamento.getUser().getId());
        dto.setTpDespesa(lancamento.getTpDespesa() != null ? lancamento.getTpDespesa().getDescricao() : null);
        dto.setTpLancamento(lancamento.getTpLancamento().toString());
        dto.setValor(lancamento.getValor().toString());
        dto.setDtLancamento(this.sdf.format(lancamento.getDtLancamento()));
        dto.setObservacao(lancamento.getObservacao());

        return dto;
    }
}
