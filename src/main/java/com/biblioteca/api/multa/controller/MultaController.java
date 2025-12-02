package com.biblioteca.api.multa.controller;

import com.biblioteca.multa.internal.entity.Multa;
import com.biblioteca.multa.open.dto.MultaCreateDTO;
import com.biblioteca.multa.open.dto.MultaDTO;
import com.biblioteca.multa.open.dto.MultaUpdateDTO;
import com.biblioteca.multa.open.service.MultaCalculoService;
import com.biblioteca.multa.open.service.MultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/multas")
@RequiredArgsConstructor
public class MultaController {

    private final MultaService multaService;
    private final MultaCalculoService multaCalculoService;

    @PostMapping
    public ResponseEntity<MultaDTO> criar(@Valid @RequestBody MultaCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(multaService.criarMulta(dto));
    }

    @PutMapping
    public ResponseEntity<MultaDTO> atualizar(@Valid @RequestBody MultaUpdateDTO dto) {
        return ResponseEntity.ok(multaService.atualizarMulta(dto));
    }

    @GetMapping
    public ResponseEntity<List<MultaDTO>> listarTodas(@RequestParam(required = false) String status) {
        if (status != null && !status.isEmpty()) {
            return ResponseEntity.ok(multaService.listarPorStatus(status));
        }
        return ResponseEntity.ok(multaService.listarTodas());
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<MultaDTO>> listarPendentes() {
        return ResponseEntity.ok(multaService.listarPendentes());
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<MultaDTO>> listarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(multaService.listarPorUsuario(idUsuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MultaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(multaService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        multaService.deletarMulta(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/por-usuario")
    public ResponseEntity<List<com.biblioteca.multa.open.dto.MultasPorUsuarioDTO>> listarPorUsuario(
            @RequestParam(required = false) String status
    ) {
        if (status != null && !status.isBlank()) {
            return ResponseEntity.ok(multaService.listarMultasPorUsuario(status));
        }
        return ResponseEntity.ok(multaService.listarMultasPorUsuario());
    }

    @PostMapping("/processar")
    public ResponseEntity<Void> processarMultas() {
        multaCalculoService.processarMultasAutomaticamente();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/pagar")
    public ResponseEntity<Void> pagar(@PathVariable Long id) {
        multaService.atualizarMulta(new com.biblioteca.multa.open.dto.MultaUpdateDTO(
                id,
                null,
                Multa.StatusMulta.PAGA,
                null
        ));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/usuario/{idUsuario}/pagar-todas")
    public ResponseEntity<Void> pagarTodas(@PathVariable Long idUsuario) {
        for (var multa : multaService.listarPorUsuario(idUsuario)) {
            if (multa.status() == Multa.StatusMulta.PENDENTE) {
                multaService.atualizarMulta(new com.biblioteca.multa.open.dto.MultaUpdateDTO(
                        multa.idMulta(),
                        null,
                        Multa.StatusMulta.PAGA,
                        null
                ));
            }
        }
        return ResponseEntity.ok().build();
    }
}
