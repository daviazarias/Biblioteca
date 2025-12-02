package com.biblioteca.multa.internal.repository;

import com.biblioteca.multa.internal.entity.Multa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MultaRepository extends JpaRepository<Multa, Long> {

    List<Multa> findByStatus(Multa.StatusMulta status);

    List<Multa> findByUsuarioIdUsuario(Long idUsuario);

    @Query("SELECT m FROM Multa m WHERE m.usuario.idUsuario = :idUsuario AND m.status = 'PENDENTE'")
    List<Multa> findMultasPendentesByUsuario(Long idUsuario);

    @Query("SELECT SUM(m.valor) FROM Multa m WHERE m.usuario.idUsuario = :idUsuario AND m.status = 'PENDENTE'")
    Double calcularTotalMultasPendentes(Long idUsuario);

    @Query("SELECT m FROM Multa m WHERE m.emprestimo.idEmprestimo = :idEmprestimo")
    java.util.Optional<Multa> findByEmprestimoIdEmprestimo(Long idEmprestimo);

    List<Multa> findByUsuarioIdUsuarioAndStatus(Long idUsuario, Multa.StatusMulta status);
}
