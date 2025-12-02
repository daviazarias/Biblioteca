package com.biblioteca.api.reserva.controller;

import com.biblioteca.reserva.open.dto.ReservaCreateDTO;
import com.biblioteca.reserva.open.dto.ReservaDTO;
import com.biblioteca.reserva.open.dto.ReservaUpdateDTO;
import com.biblioteca.reserva.open.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaDTO> criar(@Valid @RequestBody ReservaCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.criarReserva(dto));
    }

    @PutMapping
    public ResponseEntity<ReservaDTO> atualizar(@Valid @RequestBody ReservaUpdateDTO dto) {
        return ResponseEntity.ok(reservaService.atualizarReserva(dto));
    }

    @GetMapping
    public ResponseEntity<List<ReservaDTO>> listarTodas() {
        return ResponseEntity.ok(reservaService.listarTodas());
    }

    @GetMapping("/ativas")
    public ResponseEntity<List<ReservaDTO>> listarAtivas() {
        return ResponseEntity.ok(reservaService.listarAtivas());
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ReservaDTO>> listarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(reservaService.listarPorUsuario(idUsuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        reservaService.cancelarReserva(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/concluir")
    public ResponseEntity<ReservaDTO> concluirReserva(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.concluirReservaECriarEmprestimo(id));
    }

    @PostMapping("/expirar")
    public ResponseEntity<Void> expirarReservas() {
        reservaService.expirarReservas();
        return ResponseEntity.ok().build();
    }
}
