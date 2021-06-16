package com.nautilus.sistema_001.api.entities;

import com.nautilus.sistema_001.api.enums.DespesaTypeEnum;
import com.nautilus.sistema_001.api.enums.LancamentoTypeEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "lancamentos")
public class Lancamento implements Serializable {

    private static final long serialVersionUID = 2988874688680345431L;
    private Long id;
    private Users users;
    private Date dtLancamento;
    private BigDecimal valor;
    private LancamentoTypeEnum tpLancamento;
    private DespesaTypeEnum tpDespesa;
    private String observacao;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @Column(name = "dt_lancamento", nullable = false)
    public Date getDtLancamento() {
        return dtLancamento;
    }

    public void setDtLancamento(Date dtLancamento) {
        this.dtLancamento = dtLancamento;
    }

    @Column(name = "valor", nullable = false)
    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "tp_lancamento", nullable = false)
    public LancamentoTypeEnum getTpLancamento() {
        return tpLancamento;
    }

    public void setTpLancamento(LancamentoTypeEnum tpLancamento) {
        this.tpLancamento = tpLancamento;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "tp_despesa")
    public DespesaTypeEnum getTpDespesa() {
        return tpDespesa;
    }

    public void setTpDespesa(DespesaTypeEnum tpDespesa) {
        this.tpDespesa = tpDespesa;
    }

    @Column(name = "observacao")
    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @PrePersist
    public void prePersist() {
        dtLancamento = new Date();
    }

    @Override
    public String toString() {
        return "Lancamento{" +
                "id=" + id +
                ", users=" + users +
                ", dtLancamento=" + dtLancamento +
                ", valor=" + valor +
                ", tpLancamento=" + tpLancamento +
                ", tpDespesa=" + tpDespesa +
                ", observacao='" + observacao +
                '}';
    }
}
