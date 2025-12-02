package com.biblioteca.livro.open.service;

import com.biblioteca.categoria.internal.entity.Categoria;
import com.biblioteca.categoria.internal.repository.CategoriaRepository;
import com.biblioteca.livro.internal.entity.Livro;
import com.biblioteca.livro.internal.mapper.LivroMapper;
import com.biblioteca.livro.internal.repository.LivroRepository;
import com.biblioteca.livro.open.dto.LivroCreateDTO;
import com.biblioteca.livro.open.dto.LivroDTO;
import com.biblioteca.livro.open.dto.LivroUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LivroService {

    public static final String BOOK_NOT_FOUND = "Livro não encontrado";
    private final LivroRepository livroRepository;
    private final CategoriaRepository categoriaRepository;
    private final LivroMapper livroMapper;

    public LivroDTO criarLivro(LivroCreateDTO dto) {
        // Regra de negócio: verificar se ISBN já existe
        if (livroRepository.findByIsbn(dto.isbn()).isPresent()) {
            throw new IllegalArgumentException("ISBN já cadastrado no sistema");
        }
        Livro livro = livroMapper.toEntity(dto);

        // Regra de negócio: adicionar categorias ao livro
        if (dto.idsCategorias() != null && !dto.idsCategorias().isEmpty()) {
            for (Long idCategoria : dto.idsCategorias()) {
                Categoria categoria = categoriaRepository.findById(idCategoria)
                        .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada: " + idCategoria));
                livro.adicionarCategoria(categoria);
            }
        }
        Livro salvo = livroRepository.save(livro);
        return livroMapper.toDTO(salvo);
    }

    @Transactional(readOnly = true)
    public List<LivroDTO> listarTodos() {
        List<LivroDTO> list = new ArrayList<>();
        for (Livro livro : livroRepository.findAll()) {
            LivroDTO dto = livroMapper.toDTO(livro);
            list.add(dto);
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<LivroDTO> listarDisponiveis() {
        List<LivroDTO> list = new ArrayList<>();
        for (Livro livro : livroRepository.findByQuantidadeDisponivelGreaterThan(0)) {
            LivroDTO dto = livroMapper.toDTO(livro);
            list.add(dto);
        }
        return list;
    }

    @Transactional(readOnly = true)
    public LivroDTO buscarPorId(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(BOOK_NOT_FOUND));
        return livroMapper.toDTO(livro);
    }

    public void deletarLivro(Long id) {
        if (!livroRepository.existsById(id)) {
            throw new IllegalArgumentException(BOOK_NOT_FOUND);
        }
        livroRepository.deleteById(id);
    }

    public LivroDTO atualizarQuantidade(Long id, Integer novaQuantidade) {
        // Regra de negócio: quantidade não pode ser negativa
        if (novaQuantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(BOOK_NOT_FOUND));
        livro.setQuantidadeDisponivel(novaQuantidade);
        livroRepository.save(livro);
        return livroMapper.toDTO(livro);
    }

    public LivroDTO atualizarLivro(Long id, LivroUpdateDTO dto) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(BOOK_NOT_FOUND));
        livro.setTitulo(dto.titulo());
        livro.setAutor(dto.autor());
        livro.setQuantidadeDisponivel(dto.quantidadeDisponivel());

        // Atualizar categorias
        livro.getCategorias().clear();
        if (dto.idsCategorias() != null && !dto.idsCategorias().isEmpty()) {
            for (Long idCategoria : dto.idsCategorias()) {
                Categoria categoria = categoriaRepository.findById(idCategoria)
                        .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada: " + idCategoria));
                livro.adicionarCategoria(categoria);
            }
        }
        livroRepository.save(livro);
        return livroMapper.toDTO(livro);
    }

    @Transactional(readOnly = true)
    public List<Object[]> obterLivrosComEmprestimosPorCategoria() {
        return livroRepository.findLivrosComEmprestimosPorCategoria();
    }

    @Transactional(readOnly = true)
    public List<Object[]> obterLivrosMaisPopulares() {
        return livroRepository.findLivrosMaisPopulares();
    }

    @Transactional(readOnly = true)
    public List<Object[]> obterLivrosDisponiveisPorCategoria() {
        return livroRepository.findLivrosDisponiveisPorCategoria();
    }
}
