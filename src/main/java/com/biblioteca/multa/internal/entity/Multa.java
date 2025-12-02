package com.biblioteca.multa.internal.entity;

import com.biblioteca.emprestimo.internal.entity.Emprestimo;
import com.biblioteca.usuario.internal.entity.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "multa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Multa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_multa")
    private Long idMulta;

    @JsonIgnore
    @NotNull(message = "Usuário é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_emprestimo")
    private Emprestimo emprestimo;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.0", message = "Valor não pode ser negativo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @NotNull(message = "Data de geração é obrigatória")
    @Column(name = "data_geracao", nullable = false)
    private LocalDate dataGeracao;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusMulta status = StatusMulta.PENDENTE;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @PrePersist
    protected void onCreate() {
        if (dataGeracao == null) {
            dataGeracao = LocalDate.now();
        }
        if (status == null) {
            status = StatusMulta.PENDENTE;
        }
    }

    public enum StatusMulta {
        PENDENTE, PAGA, CANCELADA
    }
}
