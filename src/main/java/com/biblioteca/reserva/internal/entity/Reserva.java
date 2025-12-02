package com.biblioteca.reserva.internal.entity;

import com.biblioteca.livro.internal.entity.Livro;
import com.biblioteca.usuario.internal.entity.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reserva")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long idReserva;

    @JsonIgnore
    @NotNull(message = "Usuário é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @JsonIgnore
    @NotNull(message = "Livro é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_livro", nullable = false)
    private Livro livro;

    @NotNull(message = "Data da reserva é obrigatória")
    @Column(name = "data_reserva", nullable = false)
    private LocalDateTime dataReserva;

    @NotNull(message = "Data de validade é obrigatória")
    @Column(name = "data_validade", nullable = false)
    private LocalDate dataValidade;

    @Column(name = "data_retirada")
    private LocalDateTime dataRetirada;

    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusReserva status = StatusReserva.ATIVA;

    @PrePersist
    protected void onCreate() {
        if (dataReserva == null) {
            dataReserva = LocalDateTime.now();
        }
        if (dataValidade == null) {
            dataValidade = LocalDate.now().plusDays(3);
        }
        if (status == null) {
            status = StatusReserva.ATIVA;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (status == StatusReserva.ATIVA &&
                LocalDate.now().isAfter(dataValidade)) {
            status = StatusReserva.EXPIRADA;
        }
    }

    public enum StatusReserva {
        ATIVA, CONCLUIDA, CANCELADA, EXPIRADA
    }
}
