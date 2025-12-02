package com.biblioteca.livro.internal.mapper;

import com.biblioteca.categoria.internal.entity.Categoria;
import com.biblioteca.livro.internal.entity.Livro;
import com.biblioteca.livro.open.dto.LivroCreateDTO;
import com.biblioteca.livro.open.dto.LivroDTO;
import com.biblioteca.reserva.internal.repository.ReservaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LivroMapper {

    private final ReservaRepository reservaRepository;

    public LivroMapper(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public LivroDTO toDTO(Livro livro) {
        if (livro == null) {
            return null;
        }

        Set<String> nomesCategorias = livro.getCategorias()
                .stream()
                .map(Categoria::getNome)
                .collect(Collectors.toSet());

        long reservasAtivas = reservaRepository.countReservasAtivasByLivro(livro.getIdLivro());
        int disponivelParaEmprestimo = Math.max(0, livro.getQuantidadeDisponivel() - (int) reservasAtivas);

        return new LivroDTO(
                livro.getIdLivro(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getIsbn(),
                livro.getAnoPublicacao(),
                livro.getDataAdicao(),
                livro.getQuantidadeDisponivel(),
                livro.getQuantidadeTotal(),
                disponivelParaEmprestimo,
                (int) reservasAtivas,
                nomesCategorias
        );
    }

    public Livro toEntity(LivroCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        Livro livro = new Livro();
        livro.setTitulo(dto.titulo());
        livro.setAutor(dto.autor());
        livro.setIsbn(dto.isbn());
        livro.setAnoPublicacao(dto.anoPublicacao());
        livro.setQuantidadeDisponivel(dto.quantidadeDisponivel());
        livro.setQuantidadeTotal(dto.quantidadeDisponivel());
        livro.setDataAdicao(LocalDate.now());

        return livro;
    }
}
