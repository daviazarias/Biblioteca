package com.biblioteca.emprestimo.internal.repository;

import com.biblioteca.emprestimo.internal.entity.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByUsuarioIdUsuario(Long idUsuario);

    List<Emprestimo> findByLivroIdLivro(Long idLivro);

    List<Emprestimo> findByStatus(Emprestimo.StatusEmprestimo status);

    @Query("SELECT e FROM Emprestimo e WHERE e.status = :status AND e.usuario.idUsuario = :idUsuario")
    List<Emprestimo> findByStatusAndUsuario(
            @Param("status") Emprestimo.StatusEmprestimo status,
            @Param("idUsuario") Long idUsuario
    );

    @Query(value = """
            SELECT e.data_devolucao_prevista, u.nome as usuario_nome, l.titulo as livro_titulo, DATEDIFF(CURRENT_DATE, e.data_devolucao_prevista) as dias_atraso
            FROM emprestimo e
            INNER JOIN usuario u ON e.id_usuario = u.id_usuario
            INNER JOIN livro l ON e.id_livro = l.id_livro
            WHERE e.status IN ('ATIVO', 'ATRASADO')
            AND e.data_devolucao_prevista < CURRENT_DATE
            AND e.id_emprestimo IN (
            	SELECT e1.id_emprestimo
            	FROM emprestimo e1
            	WHERE e1.data_devolucao_real IS NULL
            )
            ORDER BY dias_atraso DESC
            """, nativeQuery = true)
    List<Object[]> findEmprestimosAtrasados();

    @Query("SELECT COUNT(e) FROM Emprestimo e WHERE e.status = 'ATIVO'")
    Long countEmprestimosAtivos();

    @Query("SELECT COUNT(e) FROM Emprestimo e WHERE e.status = 'ATRASADO'")
    Long countEmprestimosAtrasados();
}
