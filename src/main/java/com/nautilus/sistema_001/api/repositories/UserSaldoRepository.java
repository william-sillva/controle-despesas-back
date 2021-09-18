package com.nautilus.sistema_001.api.repositories;

import com.nautilus.sistema_001.api.entities.UserSaldo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserSaldoRepository extends JpaRepository<UserSaldo, Long> {

    @Query("SELECT us FROM UserSaldo us WHERE us.user.id = :userId " )
    Optional<UserSaldo> findByUserId(@Param("userId") Long userId);
}
