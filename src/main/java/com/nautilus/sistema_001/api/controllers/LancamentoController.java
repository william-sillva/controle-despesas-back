package com.nautilus.sistema_001.api.controllers;

import com.nautilus.sistema_001.api.dtos.EnumDto;
import com.nautilus.sistema_001.api.dtos.LancamentoDto;
import com.nautilus.sistema_001.api.entities.Lancamento;
import com.nautilus.sistema_001.api.entities.Users;
import com.nautilus.sistema_001.api.enums.DespesaTypeEnum;
import com.nautilus.sistema_001.api.enums.LancamentoTypeEnum;
import com.nautilus.sistema_001.api.response.Response;
import com.nautilus.sistema_001.api.services.LancamentoService;
import com.nautilus.sistema_001.api.services.UsersService;
import com.nautilus.sistema_001.api.services.impl.LancamentoServiceImpl;
import com.nautilus.sistema_001.api.services.impl.UsersServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
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

    @Configuration
    @Import({LancamentoServiceImpl.class, UsersServiceImpl.class})
    static class Config {}

    @GetMapping
    public ResponseEntity<Response<Collection<EnumDto>>> getTypesDespesas() {
        log.info("Recuperando Tipos de Despesas:");
        Response<Collection<EnumDto>> response = new Response<>();

        Collection<EnumDto> typeList = Stream.of(DespesaTypeEnum.values())
                .map(d -> new EnumDto(d.getChave(), d.getDescricao()))
                .collect(Collectors.toList());

        response.setData(typeList);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Response<LancamentoDto>> addLancamento(@Valid @RequestBody LancamentoDto lancamentoDto,
                                                                 BindingResult result) throws ParseException {

        log.info("Adicionando lancamento: {}", lancamentoDto.getId());
        Response<LancamentoDto> response = new Response<>();
        Optional<Users> user = usersService.findById(lancamentoDto.getIdUsuario());

        this.validateExistData(lancamentoDto, user, result);

        if (result.hasErrors()) {
            log.error("Erro ao validar dados do lancamento: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        Lancamento lancamento = this.convertDtoToEntity(lancamentoDto, user);
        this.lancamentoService.persist(lancamento);
        response.setData(this.convertEntityToDto(lancamento));

        return ResponseEntity.ok(response);
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

    private Optional<LancamentoTypeEnum> getLancamentoTypeEnum(String tp) {
        return Arrays.stream(LancamentoTypeEnum.values())
                .filter(lte -> lte.toString().equals(tp)).findAny();
    }

    private Optional<DespesaTypeEnum> getDespesaTypeEnum(String tp) {
        return Arrays.stream(DespesaTypeEnum.values())
                .filter(dte -> dte.getDescricao().equals(tp)).findAny();
    }

    private Lancamento convertDtoToEntity(LancamentoDto dto, Optional<Users> user) throws ParseException {
        Lancamento lancamento = new Lancamento();
        lancamento.setUsers(user.get());
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
        dto.setIdUsuario(lancamento.getUsers().getId());
        dto.setTpDespesa(lancamento.getTpDespesa() != null ? lancamento.getTpDespesa().getDescricao() : null);
        dto.setTpLancamento(lancamento.getTpLancamento().toString());
        dto.setValor(lancamento.getValor().toString());
        dto.setDtLancamento(this.sdf.format(lancamento.getDtLancamento()));
        dto.setObservacao(lancamento.getObservacao());

        return dto;
    }
}
