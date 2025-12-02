package com.biblioteca.usuario.internal.repository;

import com.biblioteca.usuario.internal.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByAtivoTrue();

    @Query("SELECT u FROM Usuario u WHERE u.nome LIKE %:nome%")
    List<Usuario> findByNomeContaining(@Param("nome") String nome);

    @Query(value = """
            SELECT u.*, COUNT(e.id_emprestimo) as total_emprestimos
            FROM usuario u
            LEFT JOIN emprestimo e ON u.id_usuario = e.id_usuario
            WHERE e.data_emprestimo BETWEEN :dataInicio AND :dataFim
            GROUP BY u.id_usuario
            ORDER BY total_emprestimos DESC
            LIMIT 10
            """, nativeQuery = true)
    List<Object[]> findUsuariosComMaisEmprestimos(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );
}
