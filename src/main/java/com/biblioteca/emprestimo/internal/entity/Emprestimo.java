package com.biblioteca.emprestimo.internal.entity;

import com.biblioteca.livro.internal.entity.Livro;
import com.biblioteca.usuario.internal.entity.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "emprestimo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"livro", "usuario"})
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_emprestimo")
    private Long idEmprestimo;

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

    @NotNull(message = "Data de empréstimo é obrigatória")
    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate dataEmprestimo;

    @NotNull(message = "Data de devolução prevista é obrigatória")
    @Column(name = "data_devolucao_prevista", nullable = false)
    private LocalDate dataDevolucaoPrevista;

    @Column(name = "data_devolucao_real")
    private LocalDate dataDevolucaoReal;

    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEmprestimo status = StatusEmprestimo.ATIVO;

    @PrePersist
    protected void onCreate() {
        if (dataEmprestimo == null) {
            dataEmprestimo = LocalDate.now();
        }
        if (dataDevolucaoPrevista == null) {
            dataDevolucaoPrevista = LocalDate.now().plusDays(14); // 14 dias padrão
        }
        // Validação: data de devolução prevista deve ser após data de empréstimo
        if (dataDevolucaoPrevista.isBefore(dataEmprestimo)) {
            throw new IllegalStateException("Data de devolução prevista deve ser após data de empréstimo");
        }
    }

    @PreUpdate
    protected void onUpdate() {
        // Atualizar status se estiver atrasado
        if (status == StatusEmprestimo.ATIVO &&
                LocalDate.now().isAfter(dataDevolucaoPrevista) &&
                dataDevolucaoReal == null) {
            status = StatusEmprestimo.ATRASADO;
        }
    }

    public enum StatusEmprestimo {
        ATIVO, DEVOLVIDO, ATRASADO
    }
}
