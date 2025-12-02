package com.biblioteca.api.relatorio.controller;

import com.biblioteca.emprestimo.open.service.EmprestimoService;
import com.biblioteca.livro.open.service.LivroService;
import com.biblioteca.usuario.open.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final LivroService livroService;
    private final UsuarioService usuarioService;
    private final EmprestimoService emprestimoService;

    @GetMapping("/livros-por-categoria")
    public ResponseEntity<List<Object[]>> getLivrosPorCategoria() {
        return ResponseEntity.ok(livroService.obterLivrosComEmprestimosPorCategoria());
    }

    @GetMapping("/livros-populares")
    public ResponseEntity<List<Object[]>> getLivrosPopulares() {
        return ResponseEntity.ok(livroService.obterLivrosMaisPopulares());
    }

    @GetMapping("/livros-disponiveis")
    public ResponseEntity<List<Object[]>> getLivrosDisponiveis() {
        return ResponseEntity.ok(livroService.obterLivrosDisponiveisPorCategoria());
    }

    @GetMapping("/emprestimos-atrasados")
    public ResponseEntity<List<Object[]>> getEmprestimosAtrasados() {
        return ResponseEntity.ok(emprestimoService.obterEmprestimosAtrasados());
    }

    @GetMapping("/usuarios-mais-emprestimos")
    public ResponseEntity<List<Object[]>> getUsuariosMaisEmprestimos(
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim
    ) {
        LocalDate inicio = dataInicio != null ? LocalDate.parse(dataInicio) : LocalDate.now().minusMonths(6);
        LocalDate fim = dataFim != null ? LocalDate.parse(dataFim) : LocalDate.now();

        return ResponseEntity.ok(usuarioService.obterUsuariosComMaisEmprestimos(inicio, fim));
    }
}
