package com.biblioteca.livro.internal.repository;

import com.biblioteca.livro.internal.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    Optional<Livro> findByIsbn(String isbn);

    List<Livro> findByQuantidadeDisponivelGreaterThan(Integer quantidade);

    @Query("SELECT l FROM Livro l WHERE l.titulo LIKE %:termo% OR l.autor LIKE %:termo%")
    List<Livro> buscarPorTituloOuAutor(@Param("termo") String termo);

    @Query(value = """
            SELECT l.titulo, c.nome as categoria_nome, COUNT(e.id_emprestimo) as total_emprestimos
            FROM livro l
            LEFT JOIN livro_categoria lc ON l.id_livro = lc.id_livro
            LEFT JOIN categoria c ON lc.id_categoria = c.id_categoria
            LEFT JOIN emprestimo e ON l.id_livro = e.id_livro
            GROUP BY l.id_livro, c.nome
            ORDER BY total_emprestimos DESC
            """, nativeQuery = true)
    List<Object[]> findLivrosComEmprestimosPorCategoria();

    @Query(value = """
            SELECT l.*, COUNT(e.id_emprestimo) as total_emprestimos
            FROM livro l
            LEFT JOIN emprestimo e ON l.id_livro = e.id_livro
            GROUP BY l.id_livro
            ORDER BY total_emprestimos DESC
            LIMIT 10
            """, nativeQuery = true)
    List<Object[]> findLivrosMaisPopulares();

    @Query(value = """
            SELECT l.titulo, c.nome, l.quantidade_disponivel
            FROM livro l
            INNER JOIN livro_categoria lc ON l.id_livro = lc.id_livro
            INNER JOIN categoria c ON lc.id_categoria = c.id_categoria
            WHERE l.quantidade_disponivel > 0
            ORDER BY c.nome, l.titulo
            """, nativeQuery = true)
    List<Object[]> findLivrosDisponiveisPorCategoria();
}
