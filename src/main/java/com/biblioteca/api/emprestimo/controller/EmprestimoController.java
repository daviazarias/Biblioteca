package com.biblioteca.api.emprestimo.controller;

import com.biblioteca.emprestimo.open.dto.EmprestimoCreateDTO;
import com.biblioteca.emprestimo.open.dto.EmprestimoDTO;
import com.biblioteca.emprestimo.open.service.EmprestimoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emprestimos")
@RequiredArgsConstructor
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    @PostMapping
    public ResponseEntity<EmprestimoDTO> criar(@Valid @RequestBody EmprestimoCreateDTO dto) {
        EmprestimoDTO criado = emprestimoService.criarEmprestimo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping
    public ResponseEntity<List<EmprestimoDTO>> listarTodos() {
        List<EmprestimoDTO> emprestimos = emprestimoService.listarTodos();
        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<EmprestimoDTO>> listarAtivos() {
        List<EmprestimoDTO> emprestimos = emprestimoService.listarAtivos();
        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/atraso")
    public ResponseEntity<List<EmprestimoDTO>> listarAtrasos() {
        List<EmprestimoDTO> emprestimos = emprestimoService.listarAtrasos();
        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<EmprestimoDTO>> listarPorUsuario(@PathVariable Long idUsuario) {
        List<EmprestimoDTO> emprestimos = emprestimoService.listarPorUsuario(idUsuario);
        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmprestimoDTO> buscarPorId(@PathVariable Long id) {
        EmprestimoDTO emprestimo = emprestimoService.buscarPorId(id);
        return ResponseEntity.ok(emprestimo);
    }

    @PatchMapping("/{id}/devolver")
    public ResponseEntity<EmprestimoDTO> devolver(@PathVariable Long id) {
        EmprestimoDTO devolvido = emprestimoService.devolverLivro(id);
        return ResponseEntity.ok(devolvido);
    }

    @PostMapping("/atualizar-atrasados")
    public ResponseEntity<Void> atualizarAtrasados() {
        emprestimoService.atualizarStatusAtrasados();
        return ResponseEntity.ok().build();
    }
}
