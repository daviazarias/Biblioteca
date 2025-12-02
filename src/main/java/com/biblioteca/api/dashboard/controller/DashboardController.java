package com.biblioteca.api.dashboard.controller;

import com.biblioteca.emprestimo.internal.repository.EmprestimoRepository;
import com.biblioteca.livro.internal.repository.LivroRepository;
import com.biblioteca.usuario.internal.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;
    private final EmprestimoRepository emprestimoRepository;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalUsuarios", usuarioRepository.count());
        stats.put("totalLivros", livroRepository.count());
        stats.put("totalEmprestimosAtivos", emprestimoRepository.countEmprestimosAtivos());
        stats.put("totalEmprestimosAtrasados", emprestimoRepository.countEmprestimosAtrasados());

        return ResponseEntity.ok(stats);
    }
}
