package com.nautilus.sistema_001.api.dtos;

import java.math.BigDecimal;
import java.util.Calendar;

public class DateBalanceDto {

    private Calendar dtInit;
    private Calendar dtFinal;
    private BigDecimal valor;

    public Calendar getDtInit() {
        return dtInit;
    }

    public void setDtInit(Calendar dtInit) {
        this.dtInit = dtInit;
    }

    public Calendar getDtFinal() {
        return dtFinal;
    }

    public void setDtFinal(Calendar dtFinal) {
        this.dtFinal = dtFinal;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
