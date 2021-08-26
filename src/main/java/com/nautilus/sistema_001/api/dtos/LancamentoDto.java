package com.nautilus.sistema_001.api.dtos;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public class LancamentoDto {

    private Optional<Long> id = Optional.empty();
    private Long usuarioId;
    private String dtLancamento;
    private String valor;
    private String tpLancamento;
    private String tpDespesa;
    private String observacao;

    public Optional<Long> getId() {
        return id;
    }

    public void setId(Optional<Long> id) {
        this.id = id;
    }

    @NotNull(message = "Id do Usuário não pode ser vazio.")
    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    @NotEmpty(message = "Data não pode ser vazia.")
    public String getDtLancamento() {
        return dtLancamento;
    }

    public void setDtLancamento(String dtLancamento) {
        this.dtLancamento = dtLancamento;
    }

    @NotEmpty(message = "Valor não pode ser vazio.")
    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @NotEmpty(message = "Tipo de Lançamento não pode ser vazio.")
    @Length(min = 1, max = 1, message = "Tipo de Lançamento deve conter apenas um caracter.")
    public String getTpLancamento() {
        return tpLancamento;
    }

    public void setTpLancamento(String tpLancamento) {
        this.tpLancamento = tpLancamento;
    }

    @Length(max = 200, message = "Tipo de Despesa não pode ter mais que 200 caracteres.")
    public String getTpDespesa() {
        return tpDespesa;
    }

    public void setTpDespesa(String tpDespesa) {
        this.tpDespesa = tpDespesa;
    }

    @Length(max = 1000, message = "Observação não pode ter mais que 1000 caracteres.")
    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public String toString() {
        return "LancamentoDto{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", dtLancamento='" + dtLancamento +
                ", valor='" + valor +
                ", tpLancamento='" + tpLancamento +
                ", tpDespesa='" + tpDespesa +
                ", observacao=" + observacao +
                '}';
    }
}
