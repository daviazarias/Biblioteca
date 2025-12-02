package com.biblioteca.api.livro.controller;

import com.biblioteca.livro.open.dto.LivroCreateDTO;
import com.biblioteca.livro.open.dto.LivroDTO;
import com.biblioteca.livro.open.dto.LivroUpdateDTO;
import com.biblioteca.livro.open.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService livroService;

    @PostMapping
    public ResponseEntity<LivroDTO> criar(@Valid @RequestBody LivroCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livroService.criarLivro(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroDTO> atualizar(@Valid @RequestBody LivroUpdateDTO dto, @PathVariable Long id) {
        return ResponseEntity.ok(livroService.atualizarLivro(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<LivroDTO>> listarTodos() {
        return ResponseEntity.ok(livroService.listarTodos());
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<LivroDTO>> listarDisponiveis() {
        return ResponseEntity.ok(livroService.listarDisponiveis());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(livroService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        livroService.deletarLivro(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/quantidade")
    public ResponseEntity<LivroDTO> atualizarQuantidade(
            @PathVariable Long id,
            @RequestParam Integer quantidade
    ) {
        return ResponseEntity.ok(livroService.atualizarQuantidade(id, quantidade));
    }
}
