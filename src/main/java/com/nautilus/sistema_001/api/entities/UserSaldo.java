package com.nautilus.sistema_001.api.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "user_saldo")
public class UserSaldo implements Serializable {

    private static final long serialVersionUID = -7810498123339310938L;
    private Long id;
    private Users user;
    private BigDecimal saldo;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToOne
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Column(name = "saldo", nullable = false)
    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "UserSaldo{" +
                "id=" + id +
                ", user=" + user +
                ", saldo=" + saldo +
                '}';
    }
}
