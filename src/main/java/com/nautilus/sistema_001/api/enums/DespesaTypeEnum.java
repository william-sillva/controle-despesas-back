package com.nautilus.sistema_001.api.enums;

public enum DespesaTypeEnum {
    ALIMENTACAO("ALIMENTACAO", "Alimentação"),
    CASA("CASA", "Casa"),
    ESPORTE("ESPORTE", "Esporte"),
    CURSOS("CURSOS", "Cursos"),
    CARRO("CARRO", "Carro"),
    MOTO("MOTO", "Moto"),
    ROUPAS("ROUPAS", "Roupas"),
    VIAGEM("VIAGEM", "Viagem"),
    OUTROS("OUTROS", "Outros");

    private final String chave;
    private final String descricao;

    DespesaTypeEnum(String chave, String descricao) {
        this.chave = chave;
        this.descricao = descricao;
    }

    public String getChave() {
        return this.chave;
    }

    public String getDescricao() {
        return this.descricao;
    }
}
