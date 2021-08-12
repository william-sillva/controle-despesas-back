package com.nautilus.sistema_001.api.dtos;

import java.util.Date;

public class LancamentoPagDto {

    private Long userId;
    private String dtInit;
    private String dtFin;
    private Integer pag;
    private String ord;
    private String dir;
    private Date dateIn;
    private Date dateFn;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDtInit() {
        return dtInit;
    }

    public void setDtInit(String dtInit) {
        this.dtInit = dtInit;
    }

    public String getDtFin() {
        return dtFin;
    }

    public void setDtFin(String dtFin) {
        this.dtFin = dtFin;
    }

    public Integer getPag() {
        if (pag == null) {
            pag = 0;
        }
        return pag;
    }

    public void setPag(Integer pag) {
        this.pag = pag;
    }

    public String getOrd() {
        if (ord.isEmpty()) {
            ord = "id";
        }
        return ord;
    }

    public void setOrd(String ord) {
        this.ord = ord;
    }

    public String getDir() {
        if (dir.isBlank()) {
            dir = "DESC";
        }
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public Date getDateIn() {
        return dateIn;
    }

    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
    }

    public Date getDateFn() {
        return dateFn;
    }

    public void setDateFn(Date dateFn) {
        this.dateFn = dateFn;
    }

    @Override
    public String toString() {
        return "LancamentoPagDto{" +
                "userId=" + userId +
                ", dtInit='" + dtInit + '\'' +
                ", dtFin='" + dtFin + '\'' +
                ", pag='" + pag + '\'' +
                ", ord='" + ord + '\'' +
                ", dir='" + dir + '\'' +
                '}';
    }
}
