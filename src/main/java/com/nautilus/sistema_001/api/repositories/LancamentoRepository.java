package com.nautilus.sistema_001.api.repositories;

import com.nautilus.sistema_001.api.entities.Lancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
@NamedQueries({
    @NamedQuery(
        name = "LancamentoRepository.findByUserId",
        query = "SELECT lanc FROM Lancamento lanc WHERE lanc.user.id = :userId"
    )
})
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    Page<Lancamento> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT lanc FROM Lancamento lanc " +
            " WHERE lanc.user.id = :userId " +
            " AND lanc.dtLancamento BETWEEN :dtIni AND :dtFin " +
            " ORDER BY lanc.dtLancamento DESC ")
    Page<Lancamento> findByFilter(
            @Param("userId") Long userId,
            @Param("dtIni") Date dtIni,
            @Param("dtFin") Date dtFin,
            Pageable pageable);

    @Query("SELECT lanc " +
            " FROM Lancamento lanc " +
            " WHERE lanc.user.id = :userId " +
            " AND lanc.dtLancamento >= :dtRef " +
            " ORDER BY lanc.dtLancamento ASC ")
    List<Lancamento> findDataGraphicByUserId(
            @Param("userId") Long userId,
            @Param("dtRef") Date dtRef);

    @Query("SELECT lanc " +
            " FROM Lancamento lanc " +
            " WHERE lanc.user.id = :userId " +
            " AND lanc.dtLancamento BETWEEN :dtIn AND :dtFin ")
    List<Lancamento> findDataBetweenByUserId(
            @Param("userId") Long userId,
            @Param("dtIn") Date dtIn,
            @Param("dtFin") Date dtFin);
}
