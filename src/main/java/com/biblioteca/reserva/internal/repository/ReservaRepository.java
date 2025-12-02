package com.biblioteca.reserva.internal.repository;

import com.biblioteca.reserva.internal.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByStatus(Reserva.StatusReserva status);

    List<Reserva> findByUsuarioIdUsuario(Long idUsuario);

    List<Reserva> findByLivroIdLivro(Long idLivro);

    @Query("SELECT r FROM Reserva r WHERE r.livro.idLivro = :idLivro AND r.status = 'ATIVA'")
    List<Reserva> findReservasAtivasByLivro(Long idLivro);

    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.livro.idLivro = :idLivro AND r.status = 'ATIVA'")
    long countReservasAtivasByLivro(Long idLivro);

    @Query("SELECT r FROM Reserva r WHERE r.status = 'ATIVA' AND r.dataValidade < :data")
    List<Reserva> findReservasExpiradas(LocalDate data);
}
